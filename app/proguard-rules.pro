-keepattributes SourceFile,LineNumberTable
-keepattributes Exceptions,InnerClasses,Signature

# Logger
-keepclassmembers class ch.qos.logback.classic.pattern.* { <init>(); }
-keep class ch.qos.logback.** { *; }
-keep class org.slf4j.impl.** { *; }
-keepattributes *Annotation*
-dontwarn ch.qos.logback.core.net.*

-keep class cn.hutool.crypto.** { *; }
-keep class com.hankcs.hanlp.** { *; }

-keepnames class * extends java.lang.Exception

# 判断SVG库是否存在 (io.noties.markwon.image.svg.SvgSupport)
-keepnames class com.caverock.androidsvg.SVG


# OKIO
-keep class okio.* { *; }

# 保持 ViewBinding 实现类中的所有名称以 “inflate” 开头的方法不被混淆
-keepclassmembers class * implements androidx.viewbinding.ViewBinding {
    public static ** inflate(...);
}

-assumenosideeffects class android.util.Log {
#    public static *** e(...);
    public static *** d(...);
    public static *** i(...);
    public static *** v(...);
#    public static *** w(...);
    public static *** wtf(...);
    public static *** println(...);
}
#-assumenosideeffects class java.io.PrintStream {
#    public *** println(...);
#    public *** print(...);
#}

# Keep `Companion` object fields of serializable classes.
# This avoids serializer lookup through `getDeclaredClasses` as done for named companion objects.
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}

# Keep `serializer()` on companion objects (both default and named) of serializable classes.
-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <2>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep `INSTANCE.serializer()` of serializable objects.
-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

# @Serializable and @Polymorphic are used at runtime for polymorphic serialization.
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

# ============================================
# lib-common 混淆规则（关键修复：代码编辑器崩溃）
# ============================================
-keep class com.github.jing332.common.** { *; }
-keepclassmembers class com.github.jing332.common.** { *; }

# ============================================
# lib-database 混淆规则（关键修复：Parcelable 反序列化）
# ============================================
-keep class com.github.jing332.database.** { *; }
-keepclassmembers class com.github.jing332.database.** { *; }
-keep class com.github.jing332.database.entities.** { *; }
-keepclassmembers class com.github.jing332.database.entities.** { *; }
-keep class com.github.jing332.database.entities.MapConverters { *; }

# 关键：IConfiguration 密封类及其所有子类不能被混淆
-keep class com.github.jing332.database.entities.systts.IConfiguration { *; }
-keep class * extends com.github.jing332.database.entities.systts.IConfiguration { *; }
-keep class com.github.jing332.database.entities.systts.** { *; }

# Parcelable 保护规则
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

# ============================================
# lib-script 混淆规则（关键修复：Gson 反序列化抽象类）
# ============================================
-keep class com.github.jing332.script.** { *; }
-keepclassmembers class com.github.jing332.script.** { *; }
# 关键：JavaScriptEngine 及其子类不能被混淆
-keep class com.github.jing332.script.JavaScriptEngine { *; }
-keep class * extends com.github.jing332.script.JavaScriptEngine { *; }

# ============================================
# lib-tts JClass 混淆规则（关键修复：Gson 反序列化抽象类）
# ============================================
# 关键：JClass 类名不能被混淆，否则 Gson 无法识别
-keep class com.github.jing332.tts.speech.plugin.engine.type.JClass {
    <init>(...);
    *;
}
# 关键：保留所有继承自 JClass 的子类
-keep class * extends com.github.jing332.tts.speech.plugin.engine.type.JClass {
    <init>(...);
    *;
}

# Gson 混淆规则
-keep class * implements com.google.gson.InstanceCreator { *; }
-keep class * extends com.google.gson.TypeAdapter { *; }
-keep class * extends com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.JsonSerializer { *; }
-keep class * extends com.google.gson.JsonDeserializer { *; }
-keep class * extends com.google.gson.Gson { *; }

# 关键修复：Gson 需要保留类的完整层次结构
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-keepattributes RuntimeVisibleAnnotations

# 保留所有可能被 Gson 序列化的数据类
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# 防止抽象类和接口被混淆导致 Gson 无法实例化
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# kotlinx.serialization 混淆规则
-keep class kotlinx.serialization.json.** { *; }
-dontwarn kotlinx.serialization.**

# Serializer for classes with named companion objects are retrieved using `getDeclaredClasses`.
# If you have any, uncomment and replace classes with those containing named companion objects.
#-keepattributes InnerClasses # Needed for `getDeclaredClasses`.
#-if @kotlinx.serialization.Serializable class
#com.example.myapplication.HasNamedCompanion, # <-- List serializable classes with named companions.
#com.example.myapplication.HasNamedCompanion2
#{
#    static **$* *;
#}
#-keepnames class <1>$$serializer { # -keepnames suffices; class is kept when serializer() is kept.
#    static <1>$$serializer INSTANCE;
#}

-dontwarn com.bumptech.glide.Glide
-dontwarn com.bumptech.glide.RequestBuilder
-dontwarn com.bumptech.glide.RequestManager
-dontwarn com.bumptech.glide.request.BaseRequestOptions
-dontwarn com.bumptech.glide.request.target.ViewTarget
-dontwarn com.squareup.picasso.Picasso
-dontwarn com.squareup.picasso.RequestCreator
-dontwarn java.awt.AWTException
-dontwarn java.awt.AlphaComposite
-dontwarn java.awt.BasicStroke
-dontwarn java.awt.Color
-dontwarn java.awt.Composite
-dontwarn java.awt.Desktop
-dontwarn java.awt.Dimension
-dontwarn java.awt.Font
-dontwarn java.awt.FontFormatException
-dontwarn java.awt.FontMetrics
-dontwarn java.awt.Graphics2D
-dontwarn java.awt.Graphics
-dontwarn java.awt.GraphicsConfiguration
-dontwarn java.awt.GraphicsDevice
-dontwarn java.awt.GraphicsEnvironment
-dontwarn java.awt.Image
-dontwarn java.awt.Point
-dontwarn java.awt.Rectangle
-dontwarn java.awt.RenderingHints$Key
-dontwarn java.awt.RenderingHints
-dontwarn java.awt.Robot
-dontwarn java.awt.Shape
-dontwarn java.awt.Stroke
-dontwarn java.awt.Toolkit
-dontwarn java.awt.color.ColorSpace
-dontwarn java.awt.datatransfer.Clipboard
-dontwarn java.awt.datatransfer.ClipboardOwner
-dontwarn java.awt.datatransfer.DataFlavor
-dontwarn java.awt.datatransfer.StringSelection
-dontwarn java.awt.datatransfer.Transferable
-dontwarn java.awt.datatransfer.UnsupportedFlavorException
-dontwarn java.awt.font.FontRenderContext
-dontwarn java.awt.geom.AffineTransform
-dontwarn java.awt.geom.Ellipse2D$Double
-dontwarn java.awt.geom.Rectangle2D
-dontwarn java.awt.geom.RoundRectangle2D$Double
-dontwarn java.awt.image.AffineTransformOp
-dontwarn java.awt.image.BufferedImage
-dontwarn java.awt.image.BufferedImageOp
-dontwarn java.awt.image.ColorConvertOp
-dontwarn java.awt.image.ColorModel
-dontwarn java.awt.image.CropImageFilter
-dontwarn java.awt.image.DataBuffer
-dontwarn java.awt.image.DataBufferByte
-dontwarn java.awt.image.DataBufferInt
-dontwarn java.awt.image.FilteredImageSource
-dontwarn java.awt.image.ImageFilter
-dontwarn java.awt.image.ImageObserver
-dontwarn java.awt.image.ImageProducer
-dontwarn java.awt.image.RenderedImage
-dontwarn java.awt.image.SampleModel
-dontwarn java.awt.image.WritableRaster
-dontwarn java.beans.BeanInfo
-dontwarn java.beans.FeatureDescriptor
-dontwarn java.beans.IntrospectionException
-dontwarn java.beans.Introspector
-dontwarn java.beans.PropertyDescriptor
-dontwarn java.beans.PropertyEditor
-dontwarn java.beans.PropertyEditorManager
-dontwarn java.beans.Transient
-dontwarn java.beans.XMLEncoder
-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.RuntimeMXBean
-dontwarn javax.imageio.IIOImage
-dontwarn javax.imageio.ImageIO
-dontwarn javax.imageio.ImageReader
-dontwarn javax.imageio.ImageTypeSpecifier
-dontwarn javax.imageio.ImageWriteParam
-dontwarn javax.imageio.ImageWriter
-dontwarn javax.imageio.metadata.IIOMetadata
-dontwarn javax.imageio.stream.ImageInputStream
-dontwarn javax.imageio.stream.ImageOutputStream
-dontwarn javax.naming.InitialContext
-dontwarn javax.naming.NamingEnumeration
-dontwarn javax.naming.NamingException
-dontwarn javax.naming.directory.Attribute
-dontwarn javax.naming.directory.Attributes
-dontwarn javax.naming.directory.InitialDirContext
-dontwarn javax.swing.ImageIcon
-dontwarn javax.tools.DiagnosticCollector
-dontwarn javax.tools.DiagnosticListener
-dontwarn javax.tools.FileObject
-dontwarn javax.tools.ForwardingJavaFileManager
-dontwarn javax.tools.JavaCompiler$CompilationTask
-dontwarn javax.tools.JavaCompiler
-dontwarn javax.tools.JavaFileManager$Location
-dontwarn javax.tools.JavaFileManager
-dontwarn javax.tools.JavaFileObject$Kind
-dontwarn javax.tools.JavaFileObject
-dontwarn javax.tools.SimpleJavaFileObject
-dontwarn javax.tools.StandardJavaFileManager
-dontwarn javax.tools.StandardLocation
-dontwarn javax.tools.ToolProvider
-dontwarn javax.xml.bind.JAXBContext
-dontwarn javax.xml.bind.Marshaller
-dontwarn javax.xml.bind.Unmarshaller
-dontwarn org.bouncycastle.asn1.ASN1Encodable
-dontwarn org.bouncycastle.asn1.ASN1InputStream
-dontwarn org.bouncycastle.asn1.ASN1Object
-dontwarn org.bouncycastle.asn1.ASN1ObjectIdentifier
-dontwarn org.bouncycastle.asn1.ASN1Primitive
-dontwarn org.bouncycastle.asn1.ASN1Sequence
-dontwarn org.bouncycastle.asn1.BERSequence
-dontwarn org.bouncycastle.asn1.DERSequence
-dontwarn org.bouncycastle.asn1.DLSequence
-dontwarn org.bouncycastle.asn1.gm.GMNamedCurves
-dontwarn org.bouncycastle.asn1.pkcs.PrivateKeyInfo
-dontwarn org.bouncycastle.asn1.sec.ECPrivateKey
-dontwarn org.bouncycastle.asn1.util.ASN1Dump
-dontwarn org.bouncycastle.asn1.x509.AlgorithmIdentifier
-dontwarn org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
-dontwarn org.bouncycastle.asn1.x9.X9ECParameters
-dontwarn org.bouncycastle.asn1.x9.X9ObjectIdentifiers
-dontwarn org.bouncycastle.cert.X509CertificateHolder
-dontwarn org.bouncycastle.crypto.AlphabetMapper
-dontwarn org.bouncycastle.crypto.BlockCipher
-dontwarn org.bouncycastle.crypto.CipherParameters
-dontwarn org.bouncycastle.crypto.CryptoException
-dontwarn org.bouncycastle.crypto.Digest
-dontwarn org.bouncycastle.crypto.InvalidCipherTextException
-dontwarn org.bouncycastle.crypto.Mac
-dontwarn org.bouncycastle.crypto.digests.SM3Digest
-dontwarn org.bouncycastle.crypto.engines.SM2Engine$Mode
-dontwarn org.bouncycastle.crypto.engines.SM2Engine
-dontwarn org.bouncycastle.crypto.engines.SM4Engine
-dontwarn org.bouncycastle.crypto.macs.CBCBlockCipherMac
-dontwarn org.bouncycastle.crypto.macs.HMac
-dontwarn org.bouncycastle.crypto.params.AsymmetricKeyParameter
-dontwarn org.bouncycastle.crypto.params.ECDomainParameters
-dontwarn org.bouncycastle.crypto.params.ECPrivateKeyParameters
-dontwarn org.bouncycastle.crypto.params.ECPublicKeyParameters
-dontwarn org.bouncycastle.crypto.params.KeyParameter
-dontwarn org.bouncycastle.crypto.params.ParametersWithID
-dontwarn org.bouncycastle.crypto.params.ParametersWithIV
-dontwarn org.bouncycastle.crypto.params.ParametersWithRandom
-dontwarn org.bouncycastle.crypto.signers.DSAEncoding
-dontwarn org.bouncycastle.crypto.signers.PlainDSAEncoding
-dontwarn org.bouncycastle.crypto.signers.SM2Signer
-dontwarn org.bouncycastle.crypto.signers.StandardDSAEncoding
-dontwarn org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey
-dontwarn org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
-dontwarn org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util
-dontwarn org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil
-dontwarn org.bouncycastle.jcajce.spec.FPEParameterSpec
-dontwarn org.bouncycastle.jcajce.spec.OpenSSHPrivateKeySpec
-dontwarn org.bouncycastle.jcajce.spec.OpenSSHPublicKeySpec
-dontwarn org.bouncycastle.jce.provider.BouncyCastleProvider
-dontwarn org.bouncycastle.jce.spec.ECNamedCurveSpec
-dontwarn org.bouncycastle.jce.spec.ECParameterSpec
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.bouncycastle.math.ec.ECCurve
-dontwarn org.bouncycastle.math.ec.ECPoint
-dontwarn org.bouncycastle.math.ec.FixedPointCombMultiplier
-dontwarn org.bouncycastle.openssl.PEMDecryptorProvider
-dontwarn org.bouncycastle.openssl.PEMEncryptedKeyPair
-dontwarn org.bouncycastle.openssl.PEMException
-dontwarn org.bouncycastle.openssl.PEMKeyPair
-dontwarn org.bouncycastle.openssl.PEMParser
-dontwarn org.bouncycastle.openssl.X509TrustedCertificateBlock
-dontwarn org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
-dontwarn org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder
-dontwarn org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder
-dontwarn org.bouncycastle.operator.InputDecryptorProvider
-dontwarn org.bouncycastle.operator.OperatorCreationException
-dontwarn org.bouncycastle.pkcs.PKCS10CertificationRequest
-dontwarn org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo
-dontwarn org.bouncycastle.pkcs.PKCSException
-dontwarn org.bouncycastle.util.Arrays
-dontwarn org.bouncycastle.util.BigIntegers
-dontwarn org.bouncycastle.util.encoders.Hex
-dontwarn org.bouncycastle.util.io.pem.PemObject
-dontwarn org.bouncycastle.util.io.pem.PemObjectGenerator
-dontwarn org.bouncycastle.util.io.pem.PemReader
-dontwarn org.bouncycastle.util.io.pem.PemWriter
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
-dontwarn org.commonmark.ext.gfm.strikethrough.Strikethrough
-dontwarn pl.droidsonroids.gif.GifDrawable

-dontwarn java.beans.BeanDescriptor
-dontwarn jdk.dynalink.CallSiteDescriptor
-dontwarn jdk.dynalink.DynamicLinker
-dontwarn jdk.dynalink.DynamicLinkerFactory
-dontwarn jdk.dynalink.NamedOperation
-dontwarn jdk.dynalink.Namespace
-dontwarn jdk.dynalink.NamespaceOperation
-dontwarn jdk.dynalink.Operation
-dontwarn jdk.dynalink.RelinkableCallSite
-dontwarn jdk.dynalink.StandardNamespace
-dontwarn jdk.dynalink.StandardOperation
-dontwarn jdk.dynalink.linker.GuardedInvocation
-dontwarn jdk.dynalink.linker.GuardingDynamicLinker
-dontwarn jdk.dynalink.linker.LinkRequest
-dontwarn jdk.dynalink.linker.LinkerServices
-dontwarn jdk.dynalink.linker.TypeBasedGuardingDynamicLinker
-dontwarn jdk.dynalink.linker.support.CompositeTypeBasedGuardingDynamicLinker
-dontwarn jdk.dynalink.linker.support.Guards
-dontwarn jdk.dynalink.support.ChainedCallSite
-dontwarn org.apache.log4j.Level
-dontwarn org.apache.log4j.Logger
-dontwarn org.apache.log4j.Priority
-dontwarn org.apache.logging.log4j.Level
-dontwarn org.apache.logging.log4j.LogManager
-dontwarn org.apache.logging.log4j.Logger
-dontwarn org.apache.logging.log4j.message.MessageFactory
-dontwarn org.apache.logging.log4j.spi.ExtendedLogger
-dontwarn org.apache.logging.log4j.spi.ExtendedLoggerWrapper
-dontwarn org.eclipse.jetty.npn.NextProtoNego$ClientProvider
-dontwarn org.eclipse.jetty.npn.NextProtoNego$Provider
-dontwarn org.eclipse.jetty.npn.NextProtoNego$ServerProvider
-dontwarn org.eclipse.jetty.npn.NextProtoNego
-dontwarn reactor.blockhound.integration.BlockHoundIntegration

-dontwarn com.aayushatharva.brotli4j.Brotli4jLoader
-dontwarn com.aayushatharva.brotli4j.decoder.DecoderJNI$Status
-dontwarn com.aayushatharva.brotli4j.decoder.DecoderJNI$Wrapper
-dontwarn com.aayushatharva.brotli4j.encoder.BrotliEncoderChannel
-dontwarn com.aayushatharva.brotli4j.encoder.Encoder$Mode
-dontwarn com.aayushatharva.brotli4j.encoder.Encoder$Parameters
-dontwarn com.github.luben.zstd.Zstd
-dontwarn com.github.luben.zstd.ZstdInputStreamNoFinalizer
-dontwarn com.github.luben.zstd.util.Native
-dontwarn com.google.protobuf.ExtensionRegistry
-dontwarn com.google.protobuf.ExtensionRegistryLite
-dontwarn com.google.protobuf.MessageLite$Builder
-dontwarn com.google.protobuf.MessageLite
-dontwarn com.google.protobuf.MessageLiteOrBuilder
-dontwarn com.google.protobuf.Parser
-dontwarn com.google.protobuf.nano.CodedOutputByteBufferNano
-dontwarn com.google.protobuf.nano.MessageNano
-dontwarn com.jcraft.jzlib.Deflater
-dontwarn com.jcraft.jzlib.Inflater
-dontwarn com.jcraft.jzlib.JZlib$WrapperType
-dontwarn com.jcraft.jzlib.JZlib
-dontwarn com.ning.compress.BufferRecycler
-dontwarn com.ning.compress.lzf.ChunkDecoder
-dontwarn com.ning.compress.lzf.ChunkEncoder
-dontwarn com.ning.compress.lzf.LZFChunk
-dontwarn com.ning.compress.lzf.LZFEncoder
-dontwarn com.ning.compress.lzf.util.ChunkDecoderFactory
-dontwarn com.ning.compress.lzf.util.ChunkEncoderFactory
-dontwarn com.oracle.svm.core.annotate.Alias
-dontwarn com.oracle.svm.core.annotate.InjectAccessors
-dontwarn com.oracle.svm.core.annotate.RecomputeFieldValue$Kind
-dontwarn com.oracle.svm.core.annotate.RecomputeFieldValue
-dontwarn com.oracle.svm.core.annotate.TargetClass
-dontwarn io.netty.internal.tcnative.AsyncSSLPrivateKeyMethod
-dontwarn io.netty.internal.tcnative.AsyncTask
-dontwarn io.netty.internal.tcnative.Buffer
-dontwarn io.netty.internal.tcnative.CertificateCallback
-dontwarn io.netty.internal.tcnative.CertificateCompressionAlgo
-dontwarn io.netty.internal.tcnative.CertificateVerifier
-dontwarn io.netty.internal.tcnative.Library
-dontwarn io.netty.internal.tcnative.ResultCallback
-dontwarn io.netty.internal.tcnative.SSL
-dontwarn io.netty.internal.tcnative.SSLContext
-dontwarn io.netty.internal.tcnative.SSLPrivateKeyMethod
-dontwarn io.netty.internal.tcnative.SSLSession
-dontwarn io.netty.internal.tcnative.SSLSessionCache
-dontwarn io.netty.internal.tcnative.SessionTicketKey
-dontwarn io.netty.internal.tcnative.SniHostNameMatcher
-dontwarn lzma.sdk.ICodeProgress
-dontwarn lzma.sdk.lzma.Encoder
-dontwarn net.jpountz.lz4.LZ4Compressor
-dontwarn net.jpountz.lz4.LZ4Exception
-dontwarn net.jpountz.lz4.LZ4Factory
-dontwarn net.jpountz.lz4.LZ4FastDecompressor
-dontwarn net.jpountz.xxhash.XXHash32
-dontwarn net.jpountz.xxhash.XXHashFactory
-dontwarn org.jboss.marshalling.ByteInput
-dontwarn org.jboss.marshalling.ByteOutput
-dontwarn org.jboss.marshalling.Marshaller
-dontwarn org.jboss.marshalling.MarshallerFactory
-dontwarn org.jboss.marshalling.MarshallingConfiguration
-dontwarn org.jboss.marshalling.Unmarshaller
-dontwarn org.osgi.annotation.bundle.Export
-dontwarn reactor.blockhound.BlockHound$Builder
-dontwarn sun.security.x509.AlgorithmId
-dontwarn sun.security.x509.CertificateAlgorithmId
-dontwarn sun.security.x509.CertificateSerialNumber
-dontwarn sun.security.x509.CertificateSubjectName
-dontwarn sun.security.x509.CertificateValidity
-dontwarn sun.security.x509.CertificateVersion
-dontwarn sun.security.x509.CertificateX509Key
-dontwarn sun.security.x509.X500Name
-dontwarn sun.security.x509.X509CertImpl
-dontwarn sun.security.x509.X509CertInfo

# ==========================================================
# EMERGENCY FIX: Keep all project classes to prevent Gson crash
# ==========================================================
-keep class com.github.jing332.tts_server_android.** { *; }
-keepclassmembers class com.github.jing332.tts_server_android.** { *; }
-keepattributes Signature, *Annotation*, EnclosingMethod
-keep class com.google.gson.** { *; }
-keep interface com.google.gson.** { *; }

# ==========================================================
# CRITICAL FIX: Rosemoe Sora CodeEditor 代码编辑器保护规则
# ==========================================================
-keep class io.github.rosemoe.sora.** { *; }
-keepclassmembers class io.github.rosemoe.sora.** { *; }
-keep class io.github.rosemoe.sora.widget.CodeEditor { *; }
-keep class io.github.rosemoe.sora.text.Content { *; }
-keep class io.github.rosemoe.sora.text.ContentListener { *; }
-keep class io.github.rosemoe.sora.lang.** { *; }
-keep class io.github.rosemoe.sora.event.** { *; }
-dontwarn io.github.rosemoe.sora.**

# CodeEditorHelper 和相关类必须保护
-keep class com.github.jing332.text_searcher.** { *; }
-keepclassmembers class com.github.jing332.text_searcher.** { *; }