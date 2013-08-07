/**
 * 
 */
package com.idyria.osi.tea.bash;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
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
 * This class starts a process and returns outputed lines
 * 
 * @author rleys
 * 
 */
public class ProcessExecutor {

	/**
	 * The original process builder
	 */
	private ProcessBuilder processBuilder = null;

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
	 * The return code
	 */
	private int returnCode = 0;

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
	 * @param processBuilder
	 */
	public ProcessExecutor(ProcessBuilder processBuilder) {
		super();
		this.processBuilder = processBuilder;
	}

	/**
	 * Receiver for output events
	 * 
	 * @author rleys
	 * 
	 */
	public static class ProcessOutputListener {

		public void stdOut(String line) {

		}

		public void stderr(String line) {

		}

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
			try {
				processBuilder.redirectErrorStream(true);
				process = processBuilder.start();

				// Set alive
				processAlive.set(true);

				// Prepare stdin pipe
				stdin = new PrintStream(process.getOutputStream());

				// -- Start ostream and errstream piping
				InputStreamPipe ostreamPipe = new InputStreamPipe(
						process.getInputStream(), stdoutDestinations);
				ostreamPipe.start();

				// Wait for the end
				returnCode = process.waitFor();
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

						// Close all ostreams
						for (WeakReference<OutputStream> osr : destinations) {
							try {
								OutputStream os = osr.get();
								if (osr != null)
									os.close();
								else {
									// FIXME Remove
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						break;
					} else {

						// Write read to all registered output streams
						for (WeakReference<OutputStream> osr : destinations) {
							try {
								OutputStream os = osr.get();
								if (osr != null)
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
	public void start(final ProcessOutputListener listener) throws IOException {

		// -- Prepare ostream reader
		PipedOutputStream os = new PipedOutputStream();
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				new PipedInputStream(os)));
		this.stdoutDestinations.offer(new WeakReference<OutputStream>(os));

		// -- Start process
		new ProcesslifeWatch().start();

		// -- Get the lines and send to listener, if necessary
		if (listener != null) {
			try {
				String line = null;
				while ((line = reader.readLine()) != null) {
					listener.stdOut(line);
				}
			} catch (IOException e) {

			}

		}

	}

}
