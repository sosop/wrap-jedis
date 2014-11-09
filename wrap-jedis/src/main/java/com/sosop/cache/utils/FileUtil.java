package com.sosop.cache.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

/**
 * @author xiaolong.hou
 * @version 0.0.1
 * @date 2014.8.29
 * @describe 文件操作, 主要针对日志和统计信息
 */

public class FileUtil {

	private final static Logger LOG = Logger.getLogger(FileUtil.class);

	/**
	 * 指定路径和内容写入文件
	 * 
	 * @param path
	 * @param value
	 */
	public static void writeToFile(String path, String value) {
		Path p = Paths.get(path);
		FileOutputStream out = null;
		FileChannel channel = null;
		try {
			if (Files.notExists(p, LinkOption.NOFOLLOW_LINKS)) {
				Files.createFile(p);
			}
			out = new FileOutputStream(p.toFile(), true);
			channel = out.getChannel();
			channel.write(MappedByteBuffer.wrap(StringUtil.append(value, "\n")
					.getBytes()));
			channel.force(true);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			try {
				if (channel != null && channel.isOpen()) {
					channel.close();
					channel = null;
				}
				if (out != null) {
					out.close();
					out = null;
				}
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 向文件中增量写文件
	 * 
	 * @param path
	 * @param value
	 */
	public static void writeToFileByLock(String path, String value) {
		RandomAccessFile rf = null;
		FileChannel fc = null;
		FileLock lock = null;
		ByteBuffer byteBuffer = null;
		try {
			rf = new RandomAccessFile(path, "rw");
			fc = rf.getChannel(); // 获得文件通道
			byteBuffer = ByteBuffer.wrap(StringUtil.append(value, "\n")
					.getBytes());
			while (true) {
				try {
					lock = fc.lock(fc.size() + 1, byteBuffer.limit(), false);// 获取阻塞锁
					break;
				} catch (OverlappingFileLockException e) {
					Thread.sleep(1 * 1000);
					LOG.info("file locked");
				}
			}
			if (null != lock) {
				fc.position(fc.size());
				fc.write(byteBuffer);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			try {
				if (lock != null && lock.isValid()) {
					lock.release();
				}
				if (fc != null && fc.isOpen()) {
					fc.close();
					fc = null;
				}
				if (rf != null) {
					rf.close();
					rf = null;
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	// 读取文件内容
	public static String read(String filePath) {
		StringBuffer sb = new StringBuffer();
		String baseDir = FileUtil.class.getResource("/").getPath();

		String fullPath = StringUtil.append(baseDir, filePath);
		
		fullPath = System.getProperty("os.name").contains("indow") ? fullPath
				.substring(1) : fullPath;

		Path path = Paths.get(fullPath);

		if (Files.notExists(path, LinkOption.NOFOLLOW_LINKS)) {
			path = path.getParent().resolveSibling(
					StringUtil.append("classes/", filePath));
		}

		try (RandomAccessFile file = new RandomAccessFile(path.toFile(), "r");
				FileChannel channel = file.getChannel();) {
			ByteBuffer buf = MappedByteBuffer.allocateDirect(512);
			while (channel.read(buf) > 0) {
				buf.flip();
				while (buf.hasRemaining()) {
					sb.append((char) buf.get());
				}
				buf.clear();
			}
		} catch (FileNotFoundException e) {
			LOG.error(e.getMessage(), e);
		} catch (IOException e1) {
			LOG.error(e1.getMessage(), e1);
		}
		return sb.toString();
	}
}