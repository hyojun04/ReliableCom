package com.example.reliablecom.client_Source;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import java.util.Arrays;

public class UDPCheckThread implements Runnable {
    private final UDPReceive receiverUdp;
    private final TcpSocketConnection tcpConnection;
    private final ImageView imageView; // Android ImageView
    private final Handler uiHandler;

    public UDPCheckThread(UDPReceive receiverUdp, TcpSocketConnection tcpConnection, ImageView imageView) {
        this.receiverUdp = receiverUdp;
        this.tcpConnection = tcpConnection;
        this.imageView = imageView;
        this.uiHandler = new Handler(Looper.getMainLooper()); // UI 작업을 처리할 Handler
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!Arrays.equals(receiverUdp.checkNewMessage, receiverUdp.lastMessage)) {
                    boolean allBitsOne = true;
                    for (byte b : receiverUdp.checkNewMessage) {
                        if (b != (byte) 0xFF) {
                            allBitsOne = false;
                            break;
                        }
                    }

                    if (allBitsOne) {
                        Log.d("UDPCheckThread", "All packets received");
                        tcpConnection.sendAckMessage_alltrue(allBitsOne);

                        boolean allRowsValid = true;
                        for (int i = 0; i < receiverUdp.imageData.length; i++) {
                            if (receiverUdp.imageData[i] == null) {
                                allRowsValid = false;
                                Log.e("UDPCheckThread", "Missing data for row: " + i);
                            }
                        }

                        if (allRowsValid) {
                            byte[] imageData1D = combineImageData(receiverUdp.imageData);
                            if (imageData1D != null) {
                                updateImageView(imageData1D);
                            } else {
                                Log.e("UDPCheckThread", "Failed to combine image data");
                            }
                        }

                        receiverUdp.initializePacketTracking();
                        //Arrays.fill(receiverUdp.checkNewMessage, (byte) 0);
                        UDPReceive.receivedMessageNum++;
                        Log.d("UDPCheckThread", "receivedMessageNum: " + UDPReceive.receivedMessageNum);
                    }

                    receiverUdp.lastMessage = Arrays.copyOf(receiverUdp.checkNewMessage, receiverUdp.checkNewMessage.length);
                }

                Thread.sleep(50);
            } catch (InterruptedException e) {
                Log.e("UDPCheckThread", "Thread was interrupted", e);
                break;
            }
        }
    }

    private byte[] combineImageData(byte[][] imageData2D) {
        if (imageData2D == null || imageData2D.length == 0) return null;

        int rows = imageData2D.length;
        final int FIXED_DATA_SIZE = 1014;
        int totalLength = rows * FIXED_DATA_SIZE;
        byte[] imageData1D = new byte[totalLength];
        int index = 0;

        for (byte[] row : imageData2D) {
            if (row != null) {
                int lengthToCopy = Math.min(row.length, FIXED_DATA_SIZE);
                System.arraycopy(row, 0, imageData1D, index, lengthToCopy);
                index += FIXED_DATA_SIZE;
            } else {
                Arrays.fill(imageData1D, index, index + FIXED_DATA_SIZE, (byte) 0);
                index += FIXED_DATA_SIZE;
            }
        }
        return imageData1D;
    }

    private void updateImageView(byte[] imageData) {
        uiHandler.post(() -> {
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    Log.e("UDPCheckThread", "Failed to decode image data");
                }
            } catch (Exception e) {
                Log.e("UDPCheckThread", "Error updating ImageView", e);
            }
        });
    }

    public static void printByteArrayAsBinary(byte[] byteArray) {
        for (byte b : byteArray) {
            //        Ʈ   0   1     ȯ
            String binaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            System.out.println(binaryString); //   ȯ
        }
    }

}
