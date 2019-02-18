package net.raysforge.commons;

import java.io.*;
import java.net.URI;

public class BetterFile extends File {

	public BetterFile(File parent, String child) {
		super(parent, child);
	}

	public BetterFile(String pathname) {
		super(pathname);
	}

	public BetterFile(String parent, String child) {
		super(parent, child);
	}

	public BetterFile(URI uri) {
		super(uri);
	}

	public BetterFile getChild(String name) {
		return new BetterFile(this, name);
	}

	public InputStream open() throws FileNotFoundException {
		return new FileInputStream(this);
	}

}
