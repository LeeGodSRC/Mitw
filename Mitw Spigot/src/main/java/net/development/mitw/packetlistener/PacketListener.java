package net.development.mitw.packetlistener;

public interface PacketListener {

	void out(PacketEvent packetEvent);
	
	void in(PacketEvent packetEvent);
	
}
