package com.okguess.admin.api.service.bastionpay;//package com.okguess.user.api.service.bastionpay;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;
//
//import java.net.URLEncoder;
//import java.security.KeyFactory;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.security.Signature;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//
//
//public class MerchantSign {
//
//    private static final Logger LOG = LoggerFactory.getLogger(MerchantSign.class);
//
//    private Signature merchantSignature;
//
//    private Signature bastionPaySignature;
//
//    private static final String ALGORITHM = "RSA";
//
//    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
//
//    private static final String DEFAULT_CHARSET = "UTF-8";
//
//    private BASE64Encoder encoder;
//
//    private BASE64Decoder decoder;
//
//    public MerchantSign(String bastionPayRsaPublicKey, String merchantRsaPrivateKey) {
//        try {
//            this.encoder = new BASE64Encoder();
//            this.decoder = new BASE64Decoder();
//
//            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
//            PrivateKey priKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decoder.decodeBuffer(merchantRsaPrivateKey)));
//
//            this.merchantSignature = Signature.getInstance(SIGN_ALGORITHMS);
//            this.merchantSignature.initSign(priKey);
//
//            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(decoder.decodeBuffer(bastionPayRsaPublicKey)));
//
//            this.bastionPaySignature = Signature.getInstance(SIGN_ALGORITHMS);
//            this.bastionPaySignature.initVerify(pubKey);
//
//        } catch (Exception e) {
//            LOG.error(e.getMessage());
//            System.exit(-1);
//        }
//    }
//
//    public String sign(String content) {
//        try {
//            this.merchantSignature.update(content.getBytes(DEFAULT_CHARSET));
//            return URLEncoder.encode(encoder.encode(this.merchantSignature.sign()).replaceAll("[\n\r]", ""), DEFAULT_CHARSET);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public boolean verify(String content, String sign) {
//        try {
//            this.bastionPaySignature.update(content.getBytes(DEFAULT_CHARSET));
//            return this.bastionPaySignature.verify(this.decoder.decodeBuffer(sign));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}
