package com.five.network.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

public class NetworkUtils {

	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	public static InetAddress getBroadcast() throws SocketException {
		System.setProperty("java.net.preferIPv4Stack", "true");
		for (Enumeration<NetworkInterface> niEnum = NetworkInterface
				.getNetworkInterfaces(); niEnum.hasMoreElements();) {
			NetworkInterface ni = niEnum.nextElement();
			if (!ni.isLoopback()) {
				for (InterfaceAddress interfaceAddress : ni
						.getInterfaceAddresses()) {

					return interfaceAddress.getBroadcast();
				}
			}
		}
		return null;
	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
		}
		return null;
	}

	public static void startSearchAirServer(AirServerDiscoveryListener listener) {
		new AirServerDiscoveryTask(listener).execute();
	}

	private static class AirServerDiscoveryTask extends
			AsyncTask<Void, Void, Void> {

		private AirServerDiscoveryListener listener;
		private String message;

		public AirServerDiscoveryTask(AirServerDiscoveryListener listener) {
			this.listener = listener;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Find the server using UDP broadcast
			try {
				// Open a random port to send the package
				DatagramSocket c = new DatagramSocket();
				c.setBroadcast(true);

				byte[] sendData = "DISCOVER_AIR_SERVER".getBytes();

				try {
					InetAddress broadCast = getBroadcast();
					DatagramPacket sendPacket = new DatagramPacket(sendData,
							sendData.length, broadCast, 8888);
					c.send(sendPacket);

				} catch (Exception e) {
					e.printStackTrace();
				}

				// Wait for a response
				byte[] recvBuf = new byte[500000];
				DatagramPacket receivePacket = new DatagramPacket(recvBuf,
						recvBuf.length);
				c.setSoTimeout(8000);
				c.receive(receivePacket);

				// Check if the message is correct
				message = new String(receivePacket.getData()).trim();

				// Close the port!
				c.close();
			} catch (IOException ex) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (message == null) {
				listener.serverNotFound();
			} else {
				listener.serverDiscovered(message);
			}
		}

	}

}
