package com.five.network.utils;

public interface AirServerDiscoveryListener {

	public void serverDiscovered(String message);
	public void serverNotFound();
}
