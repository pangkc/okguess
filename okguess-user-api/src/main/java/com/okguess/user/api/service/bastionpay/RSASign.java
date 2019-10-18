package com.okguess.user.api.service.bastionpay;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSASign {
    private static final Logger LOG = LoggerFactory.getLogger(RSASign.class);

    private Signature merchantSignature;

    private Signature bastionPaySignature;

    private static final String ALGORITHM = "RSA";

    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    private static final String DEFAULT_CHARSET = "UTF-8";

    private BASE64Encoder encoder;

    private BASE64Decoder decoder;

    public RSASign(String merchantRsaPublicKey, String bastionPayRsaPrivateKey) {
        try {
            this.encoder = new BASE64Encoder();
            this.decoder = new BASE64Decoder();

            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);

            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(decoder.decodeBuffer(merchantRsaPublicKey)));

            this.merchantSignature = Signature.getInstance(SIGN_ALGORITHMS);
            this.merchantSignature.initVerify(pubKey);


            PrivateKey priKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decoder.decodeBuffer(bastionPayRsaPrivateKey)));

            this.bastionPaySignature = Signature.getInstance(SIGN_ALGORITHMS);
            this.bastionPaySignature.initSign(priKey);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    public String sign(String content) {
        try {
            this.bastionPaySignature.update(content.getBytes(DEFAULT_CHARSET));
            return URLEncoder.encode(encoder.encode(this.bastionPaySignature.sign()).replaceAll("[\n\r]", ""), DEFAULT_CHARSET);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verify(String content, String sign) {
        try {
            sign = URLDecoder.decode(sign, DEFAULT_CHARSET);
            this.merchantSignature.update(content.getBytes(DEFAULT_CHARSET));
            return this.merchantSignature.verify(this.decoder.decodeBuffer(sign));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
