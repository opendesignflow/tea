/**
 * 
 */
package com.idyria.osi.tea.thread;

/*-
 * #%L
 * Tea Scala Utils Library
 * %%
 * Copyright (C) 2006 - 2017 Open Design Flow
 * %%
 * This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.idyria.osi.tea.logging.TeaLogging;

/**
 * @author Rtek
 * 
 */
public final class ThreadPoolManager implements ExecutorService,ScheduledExecutorService {

	// private ExecutorService threadPool = null;

	/**
	 * The singleton reference
	 */
	private static ThreadPoolManager ref = null;

	private ExecutorService executor = null;
	
	private ScheduledExecutorService scheduledExecutor = null;

	/**
	 * 
	 */
	private ThreadPoolManager() {
		// super(10,Integer.MAX_VALUE,3,TimeUnit.SECONDS,new
		// SynchronousQueue<Runnable>());

		
		// Create the ExecutorService
		//this.executor = Executors.newFixedThreadPool(5);
		this.executor = Executors.newCachedThreadPool();
		this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
	}

	public void setFixedThreadPool() {
		this.executor = Executors.newFixedThreadPool(8);
	}

	/**
	 * Get the instance of this class. Once it will ge fetched once, the next
	 * calls will lead to return a null
	 * 
	 * @return ThreadPoolManager Returns the ThreadPoolManager upon first call -
	 *         null otherwise
	 */
	public static synchronized ThreadPoolManager getInstance() {
		if (ref==null) {
			ref = new ThreadPoolManager();
		} 
		return ref;
	}

	/**
	 * Method to submit an item
	 * 
	 * @param run
	 * @return
	 */
	public synchronized Future<?> submit(Runnable run) {
		Future<?> f = null;
		synchronized (this.executor) {

			try {
				f = this.executor.submit(run);
			} catch (RejectedExecutionException ex) {
				
				f = this.executor.submit(run);
			}
		}
		return f;
	}

	public boolean awaitTermination(long arg0, TimeUnit arg1)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	

	/* (non-Javadoc)
	 * @see java.util.concurrent.ExecutorService#invokeAll(java.util.Collection, long, java.util.concurrent.TimeUnit)
	 */
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> arg0, long arg1, TimeUnit arg2) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ExecutorService#invokeAll(java.util.Collection)
	 */
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> arg0) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ExecutorService#invokeAny(java.util.Collection, long, java.util.concurrent.TimeUnit)
	 */
	public <T> T invokeAny(Collection<? extends Callable<T>> arg0, long arg1, TimeUnit arg2) throws InterruptedException, ExecutionException, TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ExecutorService#invokeAny(java.util.Collection)
	 */
	public <T> T invokeAny(Collection<? extends Callable<T>> arg0) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

	public synchronized boolean isShutdown() {
		// TODO Auto-generated method stub
		return false;
	}

	public synchronized boolean isTerminated() {
		// TODO Auto-generated method stub
		return false;
	}

	public synchronized void shutdown() {
		this.shutdownNow();

	}

	public synchronized List<Runnable> shutdownNow() {
		TeaLogging.teaLogFine("###################### SHUTDING DOWN EXECUTOR ######################");
		this.scheduledExecutor.shutdown();
		
		return this.executor.shutdownNow();
	}

	public synchronized <T> Future<T> submit(Callable<T> run) {
		Future<T> f = null;
		synchronized (this.executor) {

			try {
				f = this.executor.submit(run);
			} catch (RejectedExecutionException ex) {
				f = this.executor.submit(run);
			}
		}
		return f;
	}

	public synchronized <T> Future<T> submit(Runnable run, T t) {
		Future<T> f = null;
		synchronized (this.executor) {

			try {
				f = this.executor.submit(run,t);
			} catch (RejectedExecutionException ex) {
				f = this.executor.submit(run,t);
			}
		}
		return f;
	}

	public synchronized void execute(Runnable arg0) {
		this.submit(arg0);

	}

	@Override
	public ScheduledFuture<?> schedule(Runnable arg0, long arg1, TimeUnit arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> ScheduledFuture<V> schedule(Callable<V> arg0, long arg1,
			TimeUnit arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable arg0, long arg1,
			long arg2, TimeUnit arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable arg0, long arg1,
			long arg2, TimeUnit arg3) {
		// TODO Auto-generated method stub
		return null;
	}

}
