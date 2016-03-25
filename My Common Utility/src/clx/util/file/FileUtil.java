/**
 * 
 */
package clx.util.file;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chulx
 *
 */
public enum FileUtil {
	INSTANCE;
	
	
	/*********************
	 * 
	 * check
	 * 
	 *********************/
	public boolean ifExist (String filename) {
		if (filename == null || filename.isEmpty())
			return false;
		
		File f = new File(filename);
		 
		return f.exists();
	}
	
	
	/*********************
	 * 
	 * list
	 * 
	 *********************/
	
	/**
	 * list all files and sub folders
	 * 
	 * @param path
	 * @return
	 */
	public File [] listAll (String path) {
		if (path == null || path.isEmpty())
			return null;
		
		File folder = new File(path);
		return folder.listFiles();		
	}

	/**
	 * list files only
	 * 
	 * @param path
	 * @return
	 */
	public File [] listFiles (String path) {
		if (path == null || path.isEmpty())
			return null;
		
		File folder = new File(path);
		File[] children = folder.listFiles();
		List<File> files = new ArrayList<File>();
		for (File file : children) {
			if (file.isFile())
				files.add(file);
		}
		
		return (File[]) files.toArray();
	}
	
	/**
	 * list files with a specific extension
	 * 
	 * @param path
	 * @param ext .db for example
	 * @return
	 */
	public File [] listFilesWithExt (String path, String ext) {
		if (path == null || path.isEmpty())
			return null;

		File folder = new File(path);
		return folder.listFiles(new FilenameFilter() { 
			public boolean accept(File dir, String filename) {
				return filename.endsWith(ext); 
			}
		});
	}
	
	
	/*********************
	 * 
	 * read/write
	 * 
	 *********************/
	public String readTextFileAsString (String filename) {
		List<String> lines = readTextFileAsLines (filename);
		if (lines == null || lines.isEmpty())
			return null;
		StringBuffer buf = new StringBuffer();
		for (String line : lines) {
			if (lines != null && !line.isEmpty()) {
				buf.append(line);
			}
		}
		return buf.toString();		
		
	}

	public List<String> readTextFileAsLines (String filename) {
		if (filename == null || filename.isEmpty())
			return null;
		
		try {
			return Files.readAllLines(FileSystems.getDefault().getPath(filename), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean writeTextFile (String filename, String data) {
		if (filename == null || filename.isEmpty())
			return false;
		
		try {
			Files.write(Paths.get(filename), (byte[]) (data == null ? "".getBytes() : data.getBytes(StandardCharsets.UTF_8)));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean writeTextFile (String filename, List<String> data) {
		if (filename == null || filename.isEmpty())
			return false;
		
		try {
			Files.write(Paths.get(filename), data, StandardCharsets.UTF_8);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
