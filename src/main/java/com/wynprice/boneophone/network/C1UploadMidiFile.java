package com.wynprice.boneophone.network;

import com.wynprice.boneophone.SkeletalBand;
import com.wynprice.boneophone.midi.MidiFileHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.File;

public class C1UploadMidiFile implements IMessage {

    @SuppressWarnings("unused")
    public C1UploadMidiFile() {
    }

    private int entityID;

    private File midiFileIn;
    private byte[] abyte;

    public C1UploadMidiFile(int entityID, File midiFile) {
        this.entityID = entityID;
        this.midiFileIn = midiFile;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityID = buf.readInt();
        this.abyte = MidiFileHandler.readBytes(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityID);
        MidiFileHandler.writeBytes(MidiFileHandler.writeMidiFile(this.midiFileIn), buf);
    }

    public static class Handler extends WorldModificationsMessageHandler<C1UploadMidiFile, IMessage> {

        @Override
        protected void handleMessage(C1UploadMidiFile message, MessageContext ctx, World world, EntityPlayer player) {
            SkeletalBand.NETWORK.sendToDimension(new S2SyncAndPlayMidi(message.entityID, message.abyte), world.provider.getDimension());
        }
    }
}
