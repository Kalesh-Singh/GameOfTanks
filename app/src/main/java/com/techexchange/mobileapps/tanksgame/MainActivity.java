package com.techexchange.mobileapps.tanksgame;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button onOffButton;
    Button discoverButton;
    Button sendButton;
    ListView listView;
    TextView readMsgTextView;
    TextView connectionStatusTextView;
    EditText writeMsgEditText;

    WifiManager wifiManager;
    WifiP2pManager wifiP2pManager;
    WifiP2pManager.Channel channel;

    BroadcastReceiver receiver;
    IntentFilter intentFilter;

    List<WifiP2pDevice> peers = new ArrayList<>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;

    static final int MESSAGE_READ = 1;

    ServerThread serverThread;
    ClientThread clientThread;
    SendReceiveThread sendReceiveThread;

    Host host;
    BattlegroundView battlegroundView;

    // Actions
    static final byte GREEN_TANK_UP = 0;
    static final byte GREEN_TANK_DOWN = 1;
    static final byte GREEN_TANK_LEFT = 2;
    static final byte GREEN_TANK_RIGHT = 3;
    static final byte GREEN_TANK_SHOOT = 4;
    static final byte RED_TANK_UP = 5;
    static final byte RED_TANK_DOWN = 6;
    static final byte RED_TANK_LEFT = 7;
    static final byte RED_TANK_RIGHT = 8;
    static final byte RED_TANK_SHOOT = 9;

    enum Host {
        SERVER, CLIENT
    }

    public class ServerThread extends Thread {
        Socket socket;
        ServerSocket serverSocket;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(8888);
                socket = serverSocket.accept();
                sendReceiveThread = new SendReceiveThread(socket);
                sendReceiveThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public class ClientThread extends Thread {
        Socket socket;
        String hostAddress;

        ClientThread(InetAddress hostAddress) {
            this.hostAddress = hostAddress.getHostAddress();
            socket = new Socket();
        }

        @Override
        public void run() {
            try {
                socket.connect(new InetSocketAddress(hostAddress, 8888), 500);
                sendReceiveThread = new SendReceiveThread(socket);
                sendReceiveThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class SendReceiveThread extends Thread {
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;

        SendReceiveThread(Socket socket) {
            this.socket = socket;
            try {
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (socket != null) {
                // Listen for message
                try {
                    bytes = inputStream.read(buffer);
                    if (bytes > 0) {
                        // We received something
                        handler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        void write(byte[] bytes) {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Allow network operations on main thread for simplicity.
        StrictMode.ThreadPolicy policy
                = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initializeComponents();
        executeListener();
    }

    Handler handler = new Handler(msg -> {
        switch (msg.what) {
            case MESSAGE_READ:
                byte[] readBuffer = (byte[]) msg.obj;
//                String tempMsg = new String(readBuffer, 0, msg.arg1);
//                readMsgTextView.setText(tempMsg);

                byte action = readBuffer[0];
                battlegroundView.handleAction(action);
        }
        return true;
    });

    private void executeListener() {
        onOffButton.setOnClickListener(v -> {
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
                onOffButton.setText("Wifi On");
            } else {
                wifiManager.setWifiEnabled(true);
                onOffButton.setText("Wifi off");
            }
        });

        discoverButton.setOnClickListener(v -> {
            wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    // Successfully started discovering
                    connectionStatusTextView.setText("Discovery Started");
                }

                @Override
                public void onFailure(int reason) {
                    // Failed to start discovering
                    connectionStatusTextView.setText("Discovery failed to Start");
                }
            });
        });

        sendButton.setOnClickListener(v -> {
            String msg = writeMsgEditText.getText().toString();
            sendReceiveThread.write(msg.getBytes());
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            final WifiP2pDevice device = deviceArray[position];
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = device.deviceAddress;

            wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(),
                            "Connected to " + device.deviceName, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int reason) {
                    Toast.makeText(getApplicationContext(),
                            "Connection failed", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register the broadcast receiver
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister the broadcast receiver
        unregisterReceiver(receiver);
    }

    WifiP2pManager.PeerListListener peerListListener = peerList -> {
        if (!peerList.getDeviceList().equals(peers)) {
            peers.clear();
            peers.addAll(peerList.getDeviceList());

            deviceNameArray = new String[peers.size()];
            deviceArray = new WifiP2pDevice[peers.size()];

            int index = 0;
            for (WifiP2pDevice device : peers) {
                deviceNameArray[index] = device.deviceName;
                deviceArray[index] = device;
                index++;
            }

            ArrayAdapter<String> arrayAdapter
                    = new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_list_item_1, deviceNameArray);

            listView.setAdapter(arrayAdapter);

            if (peers.size() == 0) {
                Toast.makeText(getApplicationContext(),
                        "No Device Found!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            final InetAddress groupOwnerAddress = info.groupOwnerAddress;

            if (info.groupFormed && info.isGroupOwner) {
                // TODO: Testing
                battlegroundView = new BattlegroundView(MainActivity.this);
                setContentView(battlegroundView);
                host = Host.SERVER;

                serverThread = new ServerThread();
                serverThread.start();
            } else if (info.groupFormed) {
                // TODO: Testing
                battlegroundView = new BattlegroundView(MainActivity.this);
                setContentView(battlegroundView);
                host = Host.CLIENT;

                clientThread = new ClientThread(groupOwnerAddress);
                clientThread.start();
            }
        }
    };

    private void initializeComponents() {
        onOffButton = findViewById(R.id.onOff);
        discoverButton = findViewById(R.id.discover);
        sendButton = findViewById(R.id.sendButton);
        listView = findViewById(R.id.peerListView);
        readMsgTextView = findViewById(R.id.readMsg);
        connectionStatusTextView = findViewById(R.id.connectionStatus);
        writeMsgEditText = findViewById(R.id.writeMsg);

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            onOffButton.setText("Wifi off");
        }

        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(this, getMainLooper(), null);
        receiver = new WifiDirectBroadcastReceiver(wifiP2pManager, channel, this);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

}
