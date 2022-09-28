package ca.mattlack.rpg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class Serializer {

    // This byte array will store all the serialized data.
    private byte[] bytes = new byte[16]; // Default 16 byte capacity.
    private int used = 0; // How many bytes are used.
    private int reading = 0; // The read position, where in the array we are reading from.

    public Serializer() {
    }

    public Serializer(File file) {
        // Load from a file.
        try {
            int length = (int) file.length();
            bytes = new byte[length]; // Create a new byte array with the length of the file.
            FileInputStream fis = new FileInputStream(file); // Create a new FileInputStream.
            fis.read(bytes); // Read the bytes from the file into the byte array.
            fis.close(); // Close the stream.

            used = length; // Set the used bytes amount to the length of the file.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Serializer(byte[] arr) {
        bytes = new byte[arr.length]; // resize bytes to the length of the array.
        System.arraycopy(arr, 0, bytes, 0, arr.length); // Copy the array into the byte array.
    }

    private void ensureCapacity(int minCapacity) {

        if (minCapacity <= bytes.length) { // If we already have enough capacity then return.
            return;
        }

        int newCapacity = bytes.length; // Start with the current capacity.
        int its = 0; // Iteration counter.
        while (newCapacity < minCapacity) {
            newCapacity <<= 1; // Double the capacity.
            if (++its >= 32) {
                throw new RuntimeException("Requested capacity too large!");
            }
        }

        byte[] newBytes = new byte[newCapacity]; // Create a new byte array with the new capacity.
        System.arraycopy(bytes, 0, newBytes, 0, bytes.length); // Copy the old bytes into the new array.
        bytes = newBytes; // Set bytes to the new array.
    }

    public void writeByte(byte b) { // Write a byte.
        ensureCapacity(used + 1); // Ensure we have enough capacity.
        bytes[used++] = b; // Write the byte.
    }

    /**
     * Writes an int.
     */
    public void writeInt(int i) {
        ensureCapacity(used + 4);

        // Isolate each byte and write it.
        // Do this by shifting the int to the right by 24, then 16, then 8 bits,
        // (then 0 bits) then masking it to a single byte with
        // a bitwise AND operation with 0xFF.
        bytes[used++] = (byte) (i >> 24 & 0xFF);
        bytes[used++] = (byte) (i >> 16 & 0xFF);
        bytes[used++] = (byte) (i >> 8 & 0xFF);
        bytes[used++] = (byte) (i & 0xFF);
    }

    /**
     * Writes a long.
     */
    public void writeLong(long l) {
        ensureCapacity(used + 8);

        // Isolate each byte and write it.
        bytes[used++] = (byte) (l >> 56 & 0xFF);
        bytes[used++] = (byte) (l >> 48 & 0xFF);
        bytes[used++] = (byte) (l >> 40 & 0xFF);
        bytes[used++] = (byte) (l >> 32 & 0xFF);
        bytes[used++] = (byte) (l >> 24 & 0xFF);
        bytes[used++] = (byte) (l >> 16 & 0xFF);
        bytes[used++] = (byte) (l >> 8 & 0xFF);
        bytes[used++] = (byte) (l & 0xFF);
    }

    /**
     * Writes a string.
     */
    public void writeString(String s) {
        byte[] bytes = s.getBytes();
        writeBytes(bytes);
    }

    /**
     * Writes a UUID.
     */
    public void writeUUID(UUID id) {
        // UUIDs are stored as the two longs that make them up.
        writeLong(id.getMostSignificantBits());
        writeLong(id.getLeastSignificantBits());
    }

    /**
     * Writes a byte array.
     */
    public void writeBytes(byte[] bytes) {
        writeInt(bytes.length);

        ensureCapacity(used + bytes.length); // Ensure we have enough capacity.
        System.arraycopy(bytes, 0, this.bytes, used, bytes.length); // Copy the bytes into the byte array.
        used += bytes.length; // Increase the used bytes amount.
    }

    /**
     * Writes a serializer.
     */
    public void writeSerializer(Serializer serializer) {
        byte[] bytes = serializer.toByteArray(); // Get the bytes from the serializer.
        writeBytes(bytes); // Write the bytes.
    }

    /**
     * Reads a serializer.
     */
    public Serializer readSerializer() {
        return new Serializer(readBytes()); // Create a new serializer with the bytes from a readBytes() call.
    }

    public byte readByte() {
        return bytes[reading++];
    }

    public int readInt() {
        // Build the integer byte by byte using bitwise OR operations to append different bytes to different parts of the integer.
        int i = (bytes[reading++] & 0xFF) << 24;
        i |= (bytes[reading++] & 0xFF) << 16;
        i |= (bytes[reading++] & 0xFF) << 8;
        i |= bytes[reading++] & 0xFF;
        return i;
    }

    public long readLong() {
        // Build the long byte by byte using bitwise OR operations to append different bytes to different parts of the long.
        long l = (bytes[reading++] & 0xFFL) << 56;
        l |= (bytes[reading++] & 0xFFL) << 48;
        l |= (bytes[reading++] & 0xFFL) << 40;
        l |= (bytes[reading++] & 0xFFL) << 32;
        l |= (bytes[reading++] & 0xFFL) << 24;
        l |= (bytes[reading++] & 0xFFL) << 16;
        l |= (bytes[reading++] & 0xFFL) << 8;
        l |= bytes[reading++] & 0xFFL;
        return l;
    }

    public String readString() {
        byte[] bytes = readBytes(); // Get the bytes.
        return new String(bytes); // Create a new string with the bytes.
    }

    public UUID readUUID() {
        return new UUID(readLong(), readLong());
    }

    public byte[] readBytes() {
        int length = readInt(); // Get the length of the byte array.
        byte[] bytes = new byte[length]; // Create a new byte array with that length.
        System.arraycopy(this.bytes, reading, bytes, 0, length); // Copy the bytes into the new array.
        reading += length; // Increase the reading index.
        return bytes; // Return the bytes.
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getUsed() {
        return used;
    }

    public int getReading() {
        return reading;
    }

    public byte[] toByteArray() {
        byte[] bytes = new byte[used]; // Create a new byte array with the used bytes.
        System.arraycopy(this.bytes, 0, bytes, 0, used); // Copy the bytes into the new array.
        return bytes; // Return that array.
    }

    public void write(File file) {

        // Make sure the parent directory exists.
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        // Write the bytes to the file.
        try (FileOutputStream fos = new FileOutputStream(file)) { // This will automatically close the file when it is done.
            fos.write(toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeBoolean(boolean b) {
        writeByte(b ? (byte) 1 : (byte) 0);
    }

    public boolean readBoolean() {
        return readByte() == 1;
    }
}
