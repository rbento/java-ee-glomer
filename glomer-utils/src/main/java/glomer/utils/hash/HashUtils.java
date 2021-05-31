package glomer.utils.hash;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HashUtils {

    private static final Logger log = LoggerFactory.getLogger(HashUtils.class);

    private HashUtils() {
    }

    public static String SHA256(final String candidate) {
        return getHash(candidate, HashMode.SHA256);
    }

    public static String SHA1(final String candidate) {
        return getHash(candidate, HashMode.SHA1);
    }

    public static String MD5(final String candidate) {
        return getHash(candidate, HashMode.MD5);
    }

    public static String getHash(final String candidate, HashMode mode) {
        if (StringUtils.isNotBlank(candidate)) {
            if (null == mode) {
                mode = HashMode.MD5;
            }
            return getHash(candidate, mode, Charset.UTF8, false);
        }
        return null;
    }

    public static String getHash(final String candidate, final HashMode hashMode, final Charset charset, final boolean encodeToBase64,
            final int length) {
        String result = getHash(candidate, hashMode, charset, encodeToBase64);
        int len = (length > 0 && length <= result.length()) ? length : result.length();
        return result.substring(0, len);
    }

    public static String getHash(final String candidate, final HashMode hashMode, final Charset charset, final boolean encodeToBase64) {
        if (StringUtils.isBlank(candidate)) {
            throw new IllegalArgumentException("Cannot get a hash value for a blank input.");
        }
        byte[] hex = generateHash(candidate, hashMode, charset, encodeToBase64);
        return encodeToBase64 ? new String(hex) : hashToString(hex);
    }

    public static HashMode getRandomHashMode() {
        Random random = new Random();
        int index = random.nextInt(HashMode.values().length);
        return HashMode.values()[index];
    }

    public static String getRandomHash(final HashMode resultHashMode, final Charset charset, final boolean encodeToBase64) {

        StringBuilder result = new StringBuilder();

        Random random = new Random();

        String top = String.valueOf(random.nextInt());
        String hashTop = hashToString(generateHash(top, getRandomHashMode(), Charset.UTF8, false));
        String head = Calendar.getInstance().getTime().toString();
        String hashHead = hashToString(generateHash(head, getRandomHashMode(), Charset.UTF8, false));
        String body = String.valueOf(System.currentTimeMillis());
        String hashBody = hashToString(generateHash(body, getRandomHashMode(), Charset.UTF8, false));
        String trailer = String.valueOf(System.nanoTime());
        String hashTrailer = hashToString(generateHash(trailer, getRandomHashMode(), Charset.UTF8, false));
        String bottom = String.valueOf(random.nextInt());
        String hashBottom = hashToString(generateHash(bottom, getRandomHashMode(), Charset.UTF8, false));

        result.append(hashToString(generateHash(hashTop, getRandomHashMode(), Charset.UTF8, false)));
        result.append(hashToString(generateHash(hashHead, getRandomHashMode(), Charset.UTF8, false)));
        result.append(hashToString(generateHash(hashBody, getRandomHashMode(), Charset.UTF8, false)));
        result.append(hashToString(generateHash(hashTrailer, getRandomHashMode(), Charset.UTF8, false)));
        result.append(hashToString(generateHash(hashBottom, getRandomHashMode(), Charset.UTF8, false)));

        return hashToString(generateHash(result.toString(), resultHashMode, charset, encodeToBase64));
    }

    private static byte[] generateHash(final String candidate, final HashMode hashMode, final Charset charset, boolean encodeToBase64) {

        if (StringUtils.isBlank(candidate)) {

            throw new IllegalArgumentException("Cannot generate a hex from a blank input.");
        }

        byte[] dataToHash;

        try {

            dataToHash = candidate.getBytes(charset.getKey());

            MessageDigest md = MessageDigest.getInstance(hashMode.getKey());

            md.update(dataToHash);

            byte[] afterHash = md.digest();

            return encodeToBase64 ? Base64.getEncoder().encode(afterHash) : afterHash;

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {

            log.error("Hash option not supported.", ex);
        }

        return new byte[0];
    }

    private static String hashToString(final byte[] bytes) {

        if (bytes.length > 0) {

            StringBuilder result = new StringBuilder();

            for (int i = 0; i < bytes.length; i++) {

                int high = ((bytes[i] >> 4) & 0xf) << 4;
                int low = bytes[i] & 0xf;

                if (high == 0) {

                    result.append("0");
                }

                result.append(Integer.toHexString(high | low));
            }

            return result.toString();
        }

        return null;
    }

    public enum Charset {

        UTF8("UTF-8"), ISO88591("ISO-8859-1");

        private String key;

        private Charset(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        @Override
        public String toString() {
            return String.format("%s [ %s ]", this.getClass().getName(), getKey());
        }
    }

    public enum HashMode {

        MD5("MD5"), SHA1("SHA-1"), SHA256("SHA-256");

        private String key;

        private HashMode(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        @Override
        public String toString() {
            return String.format("%s [ %s ]", this.getClass().getName(), getKey());
        }
    }
}
