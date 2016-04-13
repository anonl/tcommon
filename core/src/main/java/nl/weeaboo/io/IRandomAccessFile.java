package nl.weeaboo.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IRandomAccessFile extends Closeable {

	/** @see InputStream#skip(long) */
	long skip(long s) throws IOException;

	/** @see InputStream#read() */
	int read() throws IOException;

	/** @see InputStream#read(byte[], int, int) */
	int read(byte[] b, int off, int len) throws IOException;

	/** @see OutputStream#write(int) */
	void write(int b) throws IOException;

	/** @see OutputStream#write(byte[], int, int) */
	void write(byte[] b, int off, int len) throws IOException;

	long pos() throws IOException;
	void seek(long pos) throws IOException;
	long length() throws IOException;

	InputStream getInputStream() throws IOException;
	InputStream getInputStream(long offset, long length) throws IOException;

}
