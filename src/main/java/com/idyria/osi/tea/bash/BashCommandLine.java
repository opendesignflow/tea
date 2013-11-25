/**
 * 
 */
package com.idyria.osi.tea.bash;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 
 * This class starts a bash command line process and manages I/O streams
 * 
 * @author rleys
 * 
 */
public class BashCommandLine {

	/**
	 * The process
	 */
	private Process process = null;

	/**
	 * Is the process alive ?
	 */
	private AtomicBoolean processAlive = new AtomicBoolean(false);

	/**
	 * Standard input to process
	 */
	private PrintStream stdin = null;

	/**
	 * Input piping default bulk
	 */
	public static int READ_BULK_SIZE = 1024;

	/**
	 * 
	 */
	public ConcurrentLinkedQueue<WeakReference<OutputStream>> stdoutDestinations = new ConcurrentLinkedQueue<WeakReference<OutputStream>>();

	/**
	 * 
	 */
	public ConcurrentLinkedQueue<WeakReference<OutputStream>> stderrDestinations = new ConcurrentLinkedQueue<WeakReference<OutputStream>>();

	/**
	 * 
	 */
	public BashCommandLine() {
		super();
	}

	/**
	 * Watches process life
	 * 
	 * @author rleys
	 * 
	 */
	private class ProcesslifeWatch extends Thread {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {

			// -- Start Process
			ProcessBuilder pb = new ProcessBuilder("bash");
			try {
				process = pb.start();

				// Set alive
				processAlive.set(true);

				// Prepare stdin pipe
				stdin = new PrintStream(process.getOutputStream());

				// -- Start ostream and errstream piping

				// Wait for the end
				process.waitFor();
				processAlive.set(false);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				processAlive.set(false);
			}

			super.run();
		}

	}

	/**
	 * Pipe and input strean (ostream or errstream) to a list of outputs
	 * 
	 * @author rleys
	 * 
	 */
	private class InputStreamPipe extends Thread {

		private InputStream targetInput = null;

		public ConcurrentLinkedQueue<WeakReference<OutputStream>> destinations = null;

		public InputStreamPipe(InputStream input,
				ConcurrentLinkedQueue<WeakReference<OutputStream>> dests) {
			super();
			this.targetInput = input;
			this.destinations = dests;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			try {
				// -- Prepare Read
				BufferedInputStream in = new BufferedInputStream(targetInput);

				while (processAlive.get()) {

					// -- Start reading
					byte[] readBytes = new byte[READ_BULK_SIZE];
					int readCount = 0;

					// Read
					readCount = in.read(readBytes);

					// EOS
					if (readCount == -1) {
						break;
					} else {

						// Write read to all registered output streams
						for (WeakReference<OutputStream> osr : destinations) {
							try {
								OutputStream os = osr.get();
								if (osr!=null)
									os.write(readBytes, 0, readCount);
								else {
									// FIXME Remove
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}

				}

				in.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Starts everything
	 * 
	 * @throws IOException
	 */
	public void start() throws IOException {

		// -- Start process
		new ProcesslifeWatch().start();

	}
	
	
	/**
	 * Starts a command and set the provided result outputstream on the list of receiver from stdout
	 * @param command
	 * @param result
	 */
	public void exec(String command,OutputStream result) {
		
		// Register ostream
		this.stdoutDestinations.offer(new WeakReference<OutputStream>(result));
		
		// Write
		this.stdin.println(command);
		
	}
	
	/**
	 * Same as exec, but creates and returns a buffered reader to is set to receive stdout
	 * @param command
	 * @return
	 * @throws IOException 
	 */
	public BufferedReader exec(String command) throws IOException {
		
		//-- Create
		PipedOutputStream os = new PipedOutputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new PipedInputStream(os)));
		
		return reader;
		
	}

}
