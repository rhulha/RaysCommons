package net.raysforge.commons;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Convenience wrappers for generating MD5 and SHA256 hashes
 * @author rhulha
 *
 */
public class HashUtils
{

    public final static String emtpySHA256Hash = "E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855";
    public final static String emtpyMD5Hash = "D41D8CD98F00B204E9800998ECF8427E";
    public static DigestInputStream getDigestInputStream(InputStream is, String hashMethod)
    {
        return new DigestInputStream(is, getMessageDigest(hashMethod));
    }

    public static DigestOutputStream getDigestOutputStream(OutputStream os, String hashMethod)
    {
        return new DigestOutputStream(os, getMessageDigest(hashMethod));
    }

    public static MessageDigest getMessageDigest(String hashMethod)
    {
        try
        {
            return MessageDigest.getInstance(hashMethod);
        } catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String getHash(String hashMethod, String s)
    {
        try
        {
            return getHash(hashMethod, s.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }

    }

    public static String getHash(String hashMethod, byte b[])
    {
        MessageDigest md = getMessageDigest(hashMethod);
        md.update(b);
        return Codecs.encodeHex(md.digest());
    }

}
