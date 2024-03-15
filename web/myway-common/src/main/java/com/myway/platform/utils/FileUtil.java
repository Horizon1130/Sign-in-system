package com.myway.platform.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.concurrent.*;

public class FileUtil implements Callable<String> {

	private MultipartFile file;
	private String path;
	private String fileName;
	private String imgBase64;
	private FileOperType type;

	public FileUtil(MultipartFile file, String path, String fileName) {
		super();
		this.file = file;
		this.path = path;
		this.fileName = fileName;
	}
	
	public FileUtil(String imgBase64, String path, String fileName) {
		super();
		this.imgBase64 = imgBase64;
		this.path = path;
		this.fileName = fileName;
	}
	
	public FileUtil(String path) {
		super();
		this.path = path;
	}

	public String saveFile() throws IOException {
		String fileName = "";
		ExecutorService serivce = Executors.newFixedThreadPool(1);
		type = FileOperType.SAVE;
		Future<String> submit = serivce.submit(this);
		try {
			fileName = submit.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return fileName;
	}

	@Override
	public String call() throws Exception {
		switch (type) {
		case SAVE:
			if (file != null) {
				InputStream is = null;
				FileOutputStream fos = null;
				try {
					is = file.getInputStream();
					String fileType = getFileType(file.getOriginalFilename());
					fileName += fileType;
//					String property = System.getProperty("user.dir");
//					path = property + "/" + path;
					File Filedir = new File(path);
					if (!Filedir.exists() || Filedir == null) {
						Filedir.mkdir();
					}
					fos = new FileOutputStream(path + fileName);
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
					}
					fos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (fos != null) {
						fos.close();
					}
					if (is != null) {
						is.close();
					}
				}
			}else if(imgBase64 != null) {
				OutputStream out = null;
				Base64 base64 = new Base64();
		        try{
		            //解密
		            byte[] b = base64.decode(imgBase64);
		            //处理数据
		            for (int i = 0;i<b.length;++i){
		                if(b[i]<0){
		                    b[i]+=256;
		                }
		            }
		            out = new FileOutputStream(path + fileName);
		            out.write(b);
		        } catch (IOException e) {
		            e.printStackTrace();
		        } finally {
		        	if (out != null) {
		        		out.flush();
			            out.close();
					}
		        }
			}
			return fileName;
			case DELETE:
				File file = new File(path);
				if (file.exists()) {
					file.delete();
				}
			break;
		}
		return null;
	}

	/**
	 * 删除文件
	 * 
	 * @param
	 *
	 */
	public void deleteFile() {
		try {
			ExecutorService serivce = Executors.newFixedThreadPool(1);
			type = FileOperType.DELETE;
			serivce.submit(this);
		} catch (Exception e) {
		}
	}

	private String getFileType(String fileName) {
		String[] s = fileName.split("\\.");
		return "." + s[s.length - 1];
	}

	public enum FileOperType {
		SAVE, DELETE;
	}
	
}
