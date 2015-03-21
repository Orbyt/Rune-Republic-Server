package com.rs.worldserver.io;

import com.rs.worldserver.Constants;

/**
 * A legal, more safe, and more efficient alternative to the "stream" class
 * found in most wL-based servers. This class performs capacity ensuring and
 * uses more familiar data-type naming conventions. It also has every method you
 * would ever need (and more, I'm afraid) to implement the RuneScape protocol,
 * and applies encryption to the data in the buffer automatically.
 * 
 * @author blakeman8192
 */
public class RSBuffer
{

	private byte[] buffer;
	private int offset, sizeOffset, bitPosition;
	private ISAACCipher encryption, decryption;
	public static int[] bitMask = new int[32];

	static
	{
		for (int i = 0; i < bitMask.length; i++)
			RSBuffer.bitMask[i] = (1 << i) - 1;
	}

	/**
	 * Initializes offset with value 0 and buffer with capacity of 128.
	 */
	public RSBuffer()
	{
		offset = 0;
		buffer = new byte[128];
	}

	/**
	 * Initializes offset with value 0 and buffer with the argued capacity.
	 * 
	 * @param capacity
	 *            The capacity of the buffer.
	 */
	public RSBuffer(int capacity)
	{
		offset = 0;
		buffer = new byte[capacity];
	}

	/**
	 * Initializes offset as the argued offset and buffer as the argued buffer.
	 * This constructor should most frequently be used for reading from the
	 * buffer.
	 * 
	 * @param buffer
	 *            The buffer to use.
	 * @param offset
	 *            The offset of the buffer.
	 */
	public RSBuffer(byte[] buffer, int offset)
	{
		this.buffer = buffer;
		this.offset = offset;
	}

	/**
	 * Gets the internal buffer.
	 */
	public byte[] getBuffer()
	{
		return buffer;
	}

	/**
	 * Sets the ISAAC cipher of this buffer. Note that this buffer generates the
	 * cipher itself from the two keys.
	 * 
	 * @param serverKey
	 *            The server key of the cipher.
	 * @param clientKey
	 *            The client key of the cipher.
	 */
	public void setISAACSeed(long serverKey, long clientKey)
	{
		int[] seed = new int[]
		{ (int) (clientKey >> 32), (int) clientKey, (int) (serverKey >> 32), (int) serverKey };
		decryption = new ISAACCipher(seed);
		for (int i : seed)
			i += 50;
		encryption = new ISAACCipher(seed);
	}

	/**
	 * Clears the buffer: sets every value inside the buffer to 0 and sets the
	 * offset to 0.
	 */
	public void clear()
	{
		for (int i = 0; i < offset; i++)
			buffer[i] = 0;
		offset = 0;
	}

	/**
	 * Gets the offset of the buffer.
	 */
	public int getOffset()
	{
		return offset;
	}

	/**
	 * Gets the encryption <code>ISAACCipher</code>.
	 */
	public ISAACCipher getEncryption()
	{
		return encryption;
	}

	/**
	 * Gets the decryption <code>ISAACCipher</code>.
	 */
	public ISAACCipher getDecryption()
	{
		return decryption;
	}

	/**
	 * Ensures that the internal buffer can have another byte written to it, if
	 * not, the buffer will be doubled in size.
	 */
	protected void ensureCapacity()
	{
		if (offset + 1 < buffer.length)
			return;
		byte[] currentBuffer = new byte[buffer.length];
		System.arraycopy(buffer, 0, currentBuffer, 0, offset);
		buffer = new byte[buffer.length * 2];
		System.arraycopy(currentBuffer, 0, buffer, 0, offset);
	}

	/**
	 * Starts bit access.
	 * 
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer startBitAccess()
	{
		bitPosition = offset * 8;
		return this;
	}

	/**
	 * Ends bit access.
	 * 
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer endBitAccess()
	{
		offset = (bitPosition + 7) / 8;
		return this;
	}

	/**
	 * Puts bits into the buffer.
	 * 
	 * @param amount
	 *            The amount of bits.
	 * @param value
	 *            The value of the bits.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putBits(int amount, int value)
	{
		int byteOffset = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		bitPosition += amount;
		for (; amount > bitOffset; bitOffset = 8)
		{
			buffer[byteOffset] &= ~bitMask[bitOffset];
			buffer[byteOffset++] |= (value >> (amount - bitOffset)) & bitMask[bitOffset];

			amount -= bitOffset;
		}
		if (amount == bitOffset)
		{
			buffer[byteOffset] &= ~bitMask[bitOffset];
			buffer[byteOffset] |= value & bitMask[bitOffset];
		} else
		{
			buffer[byteOffset] &= ~(bitMask[amount] << (bitOffset - amount));
			buffer[byteOffset] |= (value & bitMask[amount]) << (bitOffset - amount);
		}
		return this;
	}

	/**
	 * Creates a packet.
	 * 
	 * @param i
	 *            The opcode of the packet.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer createPacket(int i)
	{
		return put(i + encryption.nextInt());
	}

	/**
	 * Creates a variable-sized packet.
	 * 
	 * @param i
	 *            The opcode of the packet.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer createPacketVarSize(int i)
	{
		createPacket(i);
		sizeOffset = offset;
		return put(0);
	}

	/**
	 * Ends a variable-sized packet.
	 * 
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer endPacketVarSize()
	{
		put(offset - sizeOffset, sizeOffset);
		return this;
	}

	/**
	 * Creates a variable-sized packet where the size is written as a
	 * <code>short</code>.
	 * 
	 * @param i
	 *            The opcode of the packet.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer createPacketVarSizeShort(int i)
	{
		createPacket(i);
		sizeOffset = offset;
		return putShort(0);
	}

	/**
	 * Ends a variable-sized packet where the size is written as a
	 * <code>short</code>.
	 * 
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer endPacketVarSizeShort()
	{
		put((offset - sizeOffset) >> 8, sizeOffset++);
		put(offset - sizeOffset, sizeOffset);
		return this;
	}

	/**
	 * Puts a <code>byte</code> into the buffer.
	 * 
	 * @param i
	 *            The <code>byte</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer put(int i)
	{
		ensureCapacity();
		buffer[offset++] = (byte) i;
		return this;
	}

	/**
	 * Puts a <code>byte</code> into the argued position in the buffer.
	 * 
	 * @param i
	 *            The <code>byte</code> value to put.
	 * @param offset
	 *            The position of the byte.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer put(int i, int offset)
	{
		buffer[offset] = (byte) i;
		return this;
	}

	/**
	 * Gets a <code>byte</code> from the buffer, and decrements the offset.
	 */
	public int get()
	{
		if (offset == 0)
			throw new RuntimeException("RSBuffer underflow!");
		return buffer[offset--];
	}

	/**
	 * Gets a special-A <code>byte</code> from the buffer.
	 */
	public int getA()
	{
		return get() - 128;
	}

	/**
	 * Gets a special-C <code>byte</code> from the buffer.
	 */
	public int getC()
	{
		return -get();
	}

	/**
	 * Gets a special-S <code>byte</code> from the buffer.
	 */
	public int getS()
	{
		return 128 - get();
	}

	/**
	 * Gets an <code>short</code> from the buffer.
	 */
	public int getShort()
	{
		return (get() << 8) | get();
	}

	/**
	 * Gets a special-A <code>short</code> from the buffer.
	 */
	public int getShortA()
	{
		return (get() << 8) | getA();
	}

	/**
	 * Gets a special-C <code>short</code> from the buffer.
	 */
	public int getShortC()
	{
		return (get() << 8) | getC();
	}

	/**
	 * Gets a special-S <code>short</code> from the buffer.
	 */
	public int getShortS()
	{
		return (get() << 8) | getS();
	}

	/**
	 * Gets a little-endian <code>short</code> from the buffer.
	 */
	public int getLEShort()
	{
		return get() | (get() << 8);
	}

	/**
	 * Gets a little-endian special-A <code>short</code> from the buffer.
	 */
	public int getLEShortA()
	{
		return getA() | (get() << 8);
	}

	/**
	 * Gets a little-endian special-C <code>short</code> from the buffer.
	 */
	public int getLEShortC()
	{
		return getC() | (get() << 8);
	}

	/**
	 * Gets a little-endian special-S <code>short</code> from the buffer.
	 */
	public int getLEShortS()
	{
		return getS() | (get() << 8);
	}

	/**
	 * Gets a <code>tribyte</code> from the buffer.
	 */
	public int getTribyte()
	{
		return (get() << 16) | getShort();
	}

	/**
	 * Gets a special-A <code>tribyte</code> from the buffer.
	 */
	public int getTribyteA()
	{
		return (get() << 16) | getShortA();
	}

	/**
	 * Gets a special-C <code>tribyte</code> from the buffer.
	 */
	public int getTribyteC()
	{
		return (get() << 16) | getShortC();
	}

	/**
	 * Gets a special-S <code>tribyte</code> from the buffer.
	 */
	public int getTribyteS()
	{
		return (get() << 16) | getShortS();
	}

	/**
	 * Gets a little-endian <code>tribyte</code> from the buffer.
	 */
	public int getLETribyte()
	{
		return getShort() | (get() << 16);
	}

	/**
	 * Gets a little-endian special-A <code>tribyte</code> from the buffer.
	 */
	public int getLETribyteA()
	{
		return getShortA() | (get() << 16);
	}

	/**
	 * Gets a little-endian special-C <code>tribyte</code> from the buffer.
	 */
	public int getLETribyteC()
	{
		return getShortC() | (get() << 16);
	}

	/**
	 * Gets a little-endian special-S <code>tribyte</code> from the buffer.
	 */
	public int getLETribyteS()
	{
		return getShortS() | (get() << 16);
	}

	/**
	 * Gets an <code>int</code> from the buffer.
	 */
	public int getInt()
	{
		return (getShort() << 16) | getShort();
	}

	/**
	 * Gets an <code>int1</code> from the buffer.
	 */
	public int getInt1()
	{
		return (get() << 16) | (get() << 24) | getLEShort();
	}

	/**
	 * Gets a special-A <code>int1</code> from the buffer.
	 */
	public int getInt1A()
	{
		return (get() << 16) | (get() << 24) | getLEShortA();
	}

	/**
	 * Gets a special-C <code>int1</code> from the buffer.
	 */
	public int getInt1C()
	{
		return (get() << 16) | (get() << 24) | getLEShortC();
	}

	/**
	 * Gets a special-S <code>int1</code> from the buffer.
	 */
	public int getInt1S()
	{
		return (get() << 16) | (get() << 24) | getLEShortS();
	}

	/**
	 * Gets an <code>int2</code> from the buffer.
	 */
	public int getInt2()
	{
		return getShort() | (get() << 24) | (get() << 16);
	}

	/**
	 * Gets a special-A <code>int2</code> from the buffer.
	 */
	public int getInt2A()
	{
		return getShortA() | (get() << 24) | (get() << 16);
	}

	/**
	 * Gets a special-C <code>int2</code> from the buffer.
	 */
	public int getInt2C()
	{
		return getShortC() | (get() << 24) | (get() << 16);
	}

	/**
	 * Gets a special-S <code>int2</code> from the buffer.
	 */
	public int getInt2S()
	{
		return getShortS() | (get() << 24) | (get() << 16);
	}

	/**
	 * Gets a special-A <code>int</code> from the buffer.
	 */
	public int getIntA()
	{
		return (getShort() << 16) | getShortA();
	}

	/**
	 * Gets a special-C <code>int</code> from the buffer.
	 */
	public int getIntC()
	{
		return (getShort() << 16) | getShortC();
	}

	/**
	 * Gets a special-S <code>int</code> from the buffer.
	 */
	public int getIntS()
	{
		return (getShort() << 16) | getShortS();
	}

	/**
	 * Gets a little-endian <code>int</code> from the buffer.
	 */
	public int getLEInt()
	{
		return getShort() | (getShort() << 16);
	}

	/**
	 * Gets a little-endian special-A <code>int</code> from the buffer.
	 */
	public int getLEIntA()
	{
		return getShortA() | (getShort() << 16);
	}

	/**
	 * Gets a little-endian special-C <code>int</code> from the buffer.
	 */
	public int getLEIntC()
	{
		return getShortC() | (getShort() << 16);
	}

	/**
	 * Gets a little-endian special-S <code>int</code> from the buffer.
	 */
	public int getLEIntS()
	{
		return getShortS() | (getShort() << 16);
	}

	/**
	 * Gets a <code>long</code> from the buffer.
	 */
	public long getLong()
	{
		return (getInt() << 32L) | getInt();
	}

	/**
	 * Gets a special-A <code>long</code> from the buffer.
	 */
	public long getLongA()
	{
		return (getInt() << 32L) | getIntA();
	}

	/**
	 * Gets a special-C <code>long</code> from the buffer.
	 */
	public long getLongC()
	{
		return (getInt() << 32L) | getIntC();
	}

	/**
	 * Gets a special-S <code>long</code> from the buffer.
	 */
	public long getLongS()
	{
		return (getInt() << 32L) | getIntS();
	}

	/**
	 * Gets a little-endian <code>long</code> from the buffer.
	 */
	public long getLELong()
	{
		return getInt() | (getInt() << 32L);
	}

	/**
	 * Gets a little-endian special-A <code>long</code> from the buffer.
	 */
	public long getLELongA()
	{
		return getIntA() | (getInt() << 32L);
	}

	/**
	 * Gets a little-endian special-C <code>long</code> from the buffer.
	 */
	public long getLELongC()
	{
		return getIntC() | (getInt() << 32L);
	}

	/**
	 * Gets a little-endian special-S <code>long</code> from the buffer.
	 */
	public long getLELongS()
	{
		return getIntS() | (getInt() << 32L);
	}

	/**
	 * Gets an <code>RSString</code> from the buffer.
	 */
	public String getRSString()
	{
		int data;
		StringBuilder builder = new StringBuilder();
		while ((data = get()) != '\n')
			builder.append(data);
		return builder.toString();
	}

	/**
	 * Puts a <code>byte[]</code> into the buffer.
	 * 
	 * @param buffer
	 *            The <code>byte[]</code> to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer put(byte[] buffer)
	{
		for (byte b : buffer)
			put(b);
		return this;
	}

	/**
	 * Puts a special-A <code>byte</code> into the buffer.
	 * 
	 * @param i
	 *            The special-A <code>byte</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putA(int i)
	{
		return put(i + 128);
	}

	/**
	 * Puts a special-C <code>byte</code> into the buffer.
	 * 
	 * @param i
	 *            The special-C <code>byte</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putC(int i)
	{
		return put(-i);
	}

	/**
	 * Puts a special-S <code>byte</code> into the buffer.
	 * 
	 * @param i
	 *            The special-S <code>byte</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putS(int i)
	{
		return put(128 - i);
	}

	/**
	 * Puts a <code>short</code> into the buffer.
	 * 
	 * @param i
	 *            The <code>short</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putShort(int i)
	{
		return put(i >> 8).put(i);
	}

	/**
	 * Puts a special-A <code>short</code> into the buffer.
	 * 
	 * @param i
	 *            The special-A <code>short</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putShortA(int i)
	{
		return put(i >> 8).putA(i);
	}

	/**
	 * Puts a special-C <code>short</code> into the buffer.
	 * 
	 * @param i
	 *            The special-C <code>short</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putShortC(int i)
	{
		return put(i >> 8).putC(i);
	}

	/**
	 * Puts a special-S <code>short</code> into the buffer.
	 * 
	 * @param i
	 *            The special-S <code>short</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putShortS(int i)
	{
		return put(i >> 8).putS(i);
	}

	/**
	 * Puts a little-endian <code>short</code> into the buffer.
	 * 
	 * @param i
	 *            The <code>short</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putLEShort(int i)
	{
		return put(i).put(i >> 8);
	}

	/**
	 * Puts a little-endian special-A <code>short</code> into the buffer.
	 * 
	 * @param i
	 *            The special-A <code>short</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putLEShortA(int i)
	{
		return putA(i).put(i >> 8);
	}

	/**
	 * Puts a little-endian special-C <code>short</code> into the buffer.
	 * 
	 * @param i
	 *            The special-C <code>short</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putLEShortC(int i)
	{
		return putC(i).put(i >> 8);
	}

	/**
	 * Puts a little-endian special-S <code>short</code> into the buffer.
	 * 
	 * @param i
	 *            The special-S <code>short</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putLEShortS(int i)
	{
		return putS(i).put(i >> 8);
	}

	/**
	 * Puts a <code>tribyte</code> into the buffer.
	 * 
	 * @param i
	 *            The <code>tribyte</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putTribyte(int i)
	{
		return put(i >> 16).putShort(i);
	}

	/**
	 * Puts a special-A <code>tribyte</code> into the buffer.
	 * 
	 * @param i
	 *            The special-A <code>tribyte</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putTribyteA(int i)
	{
		return put(i >> 16).putShortA(i);
	}

	/**
	 * Puts a special-C <code>tribyte</code> into the buffer.
	 * 
	 * @param i
	 *            The special-C <code>tribyte</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putTribyteC(int i)
	{
		return put(i >> 16).putShortC(i);
	}

	/**
	 * Puts a special-S <code>tribyte</code> into the buffer.
	 * 
	 * @param i
	 *            The special-S <code>tribyte</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putTribyteS(int i)
	{
		return put(i >> 16).putShortS(i);
	}

	/**
	 * Puts a little-endian <code>tribyte</code> into the buffer.
	 * 
	 * @param i
	 *            The <code>tribyte</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putLETribyte(int i)
	{
		return putLEShort(i).put(i >> 16);
	}

	/**
	 * Puts a little-endian special-A <code>tribyte</code> into the buffer.
	 * 
	 * @param i
	 *            The special-A <code>tribyte</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putLETribyteA(int i)
	{
		return putLEShortA(i).put(i >> 16);
	}

	/**
	 * Puts a little-endian special-C <code>tribyte</code> into the buffer.
	 * 
	 * @param i
	 *            The special-C <code>tribyte</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putLETribyteC(int i)
	{
		return putLEShortC(i).put(i >> 16);
	}

	/**
	 * Puts a little-endian special-S <code>tribyte</code> into the buffer.
	 * 
	 * @param i
	 *            The special-S <code>tribyte</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putLETribyteS(int i)
	{
		return putLEShortS(i).put(i >> 16);
	}

	/**
	 * Puts an <code>int</code> into the buffer.
	 * 
	 * @param i
	 *            The <code>int</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putInt(int i)
	{
		return putShort(i >> 16).putShort(i);
	}

	/**
	 * Puts an <code>int1</code> into the buffer.
	 * 
	 * @param i
	 *            The <code>int1</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putInt1(int i)
	{
		return putShort(i).put(i >> 24).put(i >> 16);
	}

	/**
	 * Puts a special-A <code>int1</code> into the buffer.
	 * 
	 * @param i
	 *            The special-A <code>int1</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putInt1A(int i)
	{
		return putShortA(i).put(i >> 24).put(i >> 16);
	}

	/**
	 * Puts a special-C <code>int1</code> into the buffer.
	 * 
	 * @param i
	 *            The special-C <code>int1</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putInt1C(int i)
	{
		return putShortC(i).put(i >> 24).put(i >> 16);
	}

	/**
	 * Puts a special-S <code>int1</code> into the buffer.
	 * 
	 * @param i
	 *            The special-S <code>int1</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putInt1S(int i)
	{
		return putShortS(i).put(i >> 24).put(i >> 16);
	}

	/**
	 * Puts an <code>int2</code> into the buffer.
	 * 
	 * @param i
	 *            The <code>int2</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putInt2(int i)
	{
		return put(i >> 16).put(i >> 24).putLEShort(i);
	}

	/**
	 * Puts a special-A <code>int2</code> into the buffer.
	 * 
	 * @param i
	 *            The special-A <code>int2</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putInt2A(int i)
	{
		return put(i >> 16).put(i >> 24).putLEShortA(i);
	}

	/**
	 * Puts a special-C <code>int2</code> into the buffer.
	 * 
	 * @param i
	 *            The special-C <code>int2</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putInt2C(int i)
	{
		return put(i >> 16).put(i >> 24).putLEShortC(i);
	}

	/**
	 * Puts a special-S <code>int2</code> into the buffer.
	 * 
	 * @param i
	 *            The special-S <code>int2</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putInt2S(int i)
	{
		return put(i >> 16).put(i >> 24).putLEShortS(i);
	}

	/**
	 * Puts a special-A <code>int</code> into the buffer.
	 * 
	 * @param i
	 *            The special-A <code>int</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putIntA(int i)
	{
		return putShortA(i >> 16).putShortA(i);
	}

	/**
	 * Puts a special-C <code>int</code> into the buffer.
	 * 
	 * @param i
	 *            The special-C <code>int</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putIntC(int i)
	{
		return putShortC(i >> 16).putShortC(i);
	}

	/**
	 * Puts a special-S <code>int</code> into the buffer.
	 * 
	 * @param i
	 *            The special-S <code>int</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putIntS(int i)
	{
		return putShortS(i >> 16).putShortS(i);
	}

	/**
	 * Puts a little-endian <code>int</code> into the buffer.
	 * 
	 * @param i
	 *            The <code>int</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putLEInt(int i)
	{
		return putLEShort(i >> 16).putLEShort(i);
	}

	/**
	 * Puts a little-endian special-A <code>int</code> into the buffer.
	 * 
	 * @param i
	 *            The special-A <code>int</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putLEIntA(int i)
	{
		return putLEShortA(i >> 16).putLEShortA(i);
	}

	/**
	 * Puts a little-endian special-C <code>int</code> into the buffer.
	 * 
	 * @param i
	 *            The special-C <code>int</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putLEIntC(int i)
	{
		return putLEShortC(i >> 16).putLEShortC(i);
	}

	/**
	 * Puts a little-endian special-S <code>int</code> into the buffer.
	 * 
	 * @param i
	 *            The special-S <code>int</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putLEIntS(int i)
	{
		return putLEShortS(i >> 16).putLEShortS(i);
	}

	/**
	 * Puts a <code>long</code> into the buffer.
	 * 
	 * @param i
	 *            The <code>long</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putLong(long i)
	{
		return putInt((int) (i >> 32)).putInt((int) i);
	}

	/**
	 * Puts a special-A <code>long</code> into the buffer.
	 * 
	 * @param i
	 *            The special-A <code>long</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putLongA(long i)
	{
		return putIntA((int) (i >> 32)).putIntA((int) i);
	}

	/**
	 * Puts a special-C <code>long</code> into the buffer.
	 * 
	 * @param i
	 *            The special-C <code>long</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putLongC(long i)
	{
		return putIntC((int) (i >> 32)).putIntC((int) i);
	}

	/**
	 * Puts a special-S <code>long</code> into the buffer.
	 * 
	 * @param i
	 *            The special-S <code>long</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putLongS(long i)
	{
		return putIntS((int) (i >> 32)).putIntS((int) i);
	}

	/**
	 * Puts a little-endian <code>long</code> into the buffer.
	 * 
	 * @param i
	 *            The <code>long</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putLELong(long i)
	{
		return putLEInt((int) i).putLEInt((int) (i >> 32));
	}

	/**
	 * Puts a little-endian special-A <code>long</code> into the buffer.
	 * 
	 * @param i
	 *            The special-A <code>long</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putLELongA(long i)
	{
		return putLEIntA((int) i).putLEIntA((int) (i >> 32));
	}

	/**
	 * Puts a little-endian special-C <code>long</code> into the buffer.
	 * 
	 * @param i
	 *            The special-C <code>long</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putLELongC(long i)
	{
		return putLEIntC((int) i).putLEIntC((int) (i >> 32));
	}

	/**
	 * Puts a little-endian special-S <code>long</code> into the buffer.
	 * 
	 * @param i
	 *            The special-S <code>long</code> value to put.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putLELongS(long i)
	{
		return putLEIntS((int) i).putLEIntS((int) (i >> 32));
	}

	/**
	 * Puts an <code>RSString</code> into the buffer.
	 * 
	 * @param s
	 *            The <code>RSString</code> value to write.
	 * @return The instance of this class, for chaining.
	 */
	public RSBuffer putRSString(String s)
	{
		for (byte b : s.getBytes())
			put(b);
		return put('\n');
	}

	private class ISAACCipher
	{
		private static final int SIZEL = 8;
		private static final int SIZE = 1 << SIZEL;
		private static final int MASK = (SIZE - 1) << 2;
		private int count;
		private int[] rsl;
		private int[] mem;
		private int a;
		private int b;
		private int c;

		/**
		 * This constructor creates and initializes an new instance using a
		 * user-provided seed.<br>
		 * Equivalent to <code>randinit(ctx, TRUE)</code> after putting seed in
		 * <code>randctx</code> in the C implementation.
		 * 
		 * @param seed
		 *            The seed.
		 */
		private ISAACCipher(int[] seed)
		{
			mem = new int[SIZE];
			rsl = new int[SIZE];
			System.arraycopy(seed, 0, rsl, 0, (seed.length <= rsl.length) ? seed.length : rsl.length);
			init(true);
		}

		/**
		 * Generate 256 results.<br>
		 * This is a small (not fast) implementation.
		 */
		private final void isaac()
		{
			int i, x, y;
			b += ++c;
			for (i = 0; i < SIZE; ++i)
			{
				x = mem[i];
				switch (i & 3)
				{
				case 0:
					a ^= a << 13;
					break;
				case 1:
					a ^= a >>> 6;
					break;
				case 2:
					a ^= a << 2;
					break;
				case 3:
					a ^= a >>> 16;
					break;
				}
				a += mem[(i + SIZE / 2) & (SIZE - 1)];
				mem[i] = y = mem[((x) & MASK) >> 2] + a + b;
				rsl[i] = b = mem[((y >> SIZEL) & MASK) >> 2] + x;
			}
		}

		/**
		 * Initialize or reinitialize this instance.
		 * 
		 * @param flag
		 *            If <code>true</code> then use the seed (which the
		 *            constructor placed in <code>rsl[]</code>) for
		 *            initialization.
		 */
		private final void init(boolean flag)
		{
			int i;
			int a, b, c, d, e, f, g, h;
			a = b = c = d = e = f = g = h = 0x9e3779b9;
			for (i = 0; i < 4; ++i)
			{
				a ^= b << 11;
				d += a;
				b += c;
				b ^= c >>> 2;
				e += b;
				c += d;
				c ^= d << 8;
				f += c;
				d += e;
				d ^= e >>> 16;
				g += d;
				e += f;
				e ^= f << 10;
				h += e;
				f += g;
				f ^= g >>> 4;
				a += f;
				g += h;
				g ^= h << 8;
				b += g;
				h += a;
				h ^= a >>> 9;
				c += h;
				a += b;
			}
			for (i = 0; i < SIZE; i += 8)
			{
				if (flag)
				{
					a += rsl[i];
					b += rsl[i + 1];
					c += rsl[i + 2];
					d += rsl[i + 3];
					e += rsl[i + 4];
					f += rsl[i + 5];
					g += rsl[i + 6];
					h += rsl[i + 7];
				}
				a ^= b << 11;
				d += a;
				b += c;
				b ^= c >>> 2;
				e += b;
				c += d;
				c ^= d << 8;
				f += c;
				d += e;
				d ^= e >>> 16;
				g += d;
				e += f;
				e ^= f << 10;
				h += e;
				f += g;
				f ^= g >>> 4;
				a += f;
				g += h;
				g ^= h << 8;
				b += g;
				h += a;
				h ^= a >>> 9;
				c += h;
				a += b;
				mem[i] = a;
				mem[i + 1] = b;
				mem[i + 2] = c;
				mem[i + 3] = d;
				mem[i + 4] = e;
				mem[i + 5] = f;
				mem[i + 6] = g;
				mem[i + 7] = h;
			}
			if (flag)
			{
				for (i = 0; i < SIZE; i += 8)
				{
					a += mem[i];
					b += mem[i + 1];
					c += mem[i + 2];
					d += mem[i + 3];
					e += mem[i + 4];
					f += mem[i + 5];
					g += mem[i + 6];
					h += mem[i + 7];
					a ^= b << 11;
					d += a;
					b += c;
					b ^= c >>> 2;
					e += b;
					c += d;
					c ^= d << 8;
					f += c;
					d += e;
					d ^= e >>> 16;
					g += d;
					e += f;
					e ^= f << 10;
					h += e;
					f += g;
					f ^= g >>> 4;
					a += f;
					g += h;
					g ^= h << 8;
					b += g;
					h += a;
					h ^= a >>> 9;
					c += h;
					a += b;
					mem[i] = a;
					mem[i + 1] = b;
					mem[i + 2] = c;
					mem[i + 3] = d;
					mem[i + 4] = e;
					mem[i + 5] = f;
					mem[i + 6] = g;
					mem[i + 7] = h;
				}
			}
			isaac();
			count = SIZE;
		}

		/**
		 * Get a random integer value.
		 */
		public final int nextInt()
		{
			if (0 == count--)
			{
				isaac();
				count = SIZE - 1;
			}

			return (rsl[count]);
		}

	}

}