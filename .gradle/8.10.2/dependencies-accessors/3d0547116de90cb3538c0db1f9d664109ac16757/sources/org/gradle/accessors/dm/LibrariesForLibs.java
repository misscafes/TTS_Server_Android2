package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the {@code libs} extension.
 */
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final AboutLibraryAccessors laccForAboutLibraryAccessors = new AboutLibraryAccessors(owner);
    private final AccompanistLibraryAccessors laccForAccompanistLibraryAccessors = new AccompanistLibraryAccessors(owner);
    private final ActivityLibraryAccessors laccForActivityLibraryAccessors = new ActivityLibraryAccessors(owner);
    private final AndroidLibraryAccessors laccForAndroidLibraryAccessors = new AndroidLibraryAccessors(owner);
    private final ApacheLibraryAccessors laccForApacheLibraryAccessors = new ApacheLibraryAccessors(owner);
    private final CodeLibraryAccessors laccForCodeLibraryAccessors = new CodeLibraryAccessors(owner);
    private final CoilLibraryAccessors laccForCoilLibraryAccessors = new CoilLibraryAccessors(owner);
    private final ComposeLibraryAccessors laccForComposeLibraryAccessors = new ComposeLibraryAccessors(owner);
    private final ConstraintlayoutLibraryAccessors laccForConstraintlayoutLibraryAccessors = new ConstraintlayoutLibraryAccessors(owner);
    private final EspressoLibraryAccessors laccForEspressoLibraryAccessors = new EspressoLibraryAccessors(owner);
    private final ExtLibraryAccessors laccForExtLibraryAccessors = new ExtLibraryAccessors(owner);
    private final FloatingxLibraryAccessors laccForFloatingxLibraryAccessors = new FloatingxLibraryAccessors(owner);
    private final HutoolLibraryAccessors laccForHutoolLibraryAccessors = new HutoolLibraryAccessors(owner);
    private final KotlinLibraryAccessors laccForKotlinLibraryAccessors = new KotlinLibraryAccessors(owner);
    private final KotlinxLibraryAccessors laccForKotlinxLibraryAccessors = new KotlinxLibraryAccessors(owner);
    private final KtorLibraryAccessors laccForKtorLibraryAccessors = new KtorLibraryAccessors(owner);
    private final LifecycleLibraryAccessors laccForLifecycleLibraryAccessors = new LifecycleLibraryAccessors(owner);
    private final LogbackLibraryAccessors laccForLogbackLibraryAccessors = new LogbackLibraryAccessors(owner);
    private final MarkwonLibraryAccessors laccForMarkwonLibraryAccessors = new MarkwonLibraryAccessors(owner);
    private final Media3LibraryAccessors laccForMedia3LibraryAccessors = new Media3LibraryAccessors(owner);
    private final MozillaLibraryAccessors laccForMozillaLibraryAccessors = new MozillaLibraryAccessors(owner);
    private final NavigationLibraryAccessors laccForNavigationLibraryAccessors = new NavigationLibraryAccessors(owner);
    private final Slf4jLibraryAccessors laccForSlf4jLibraryAccessors = new Slf4jLibraryAccessors(owner);
    private final SplittiesLibraryAccessors laccForSplittiesLibraryAccessors = new SplittiesLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

    /**
     * Dependency provider for <b>activityKtx</b> with <b>androidx.activity:activity-ktx</b> coordinates and
     * with version reference <b>activityKtx</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getActivityKtx() {
        return create("activityKtx");
    }

    /**
     * Dependency provider for <b>appcompat</b> with <b>androidx.appcompat:appcompat</b> coordinates and
     * with version reference <b>appcompat</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getAppcompat() {
        return create("appcompat");
    }

    /**
     * Dependency provider for <b>coreKtx</b> with <b>androidx.core:core-ktx</b> coordinates and
     * with version reference <b>coreKtx</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getCoreKtx() {
        return create("coreKtx");
    }

    /**
     * Dependency provider for <b>desugar</b> with <b>com.android.tools:desugar_jdk_libs</b> coordinates and
     * with version reference <b>desugar</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getDesugar() {
        return create("desugar");
    }

    /**
     * Dependency provider for <b>hanlp</b> with <b>com.hankcs:hanlp</b> coordinates and
     * with version reference <b>hanlp</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getHanlp() {
        return create("hanlp");
    }

    /**
     * Dependency provider for <b>junit</b> with <b>junit:junit</b> coordinates and
     * with version reference <b>junit</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getJunit() {
        return create("junit");
    }

    /**
     * Dependency provider for <b>localbroadcast</b> with <b>androidx.localbroadcastmanager:localbroadcastmanager</b> coordinates and
     * with version <b>1.0.0</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getLocalbroadcast() {
        return create("localbroadcast");
    }

    /**
     * Dependency provider for <b>logging</b> with <b>io.github.oshai:kotlin-logging-jvm</b> coordinates and
     * with version reference <b>logging</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getLogging() {
        return create("logging");
    }

    /**
     * Dependency provider for <b>net</b> with <b>com.github.liangjingkanji:Net</b> coordinates and
     * with version reference <b>net</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getNet() {
        return create("net");
    }

    /**
     * Dependency provider for <b>okhttp</b> with <b>com.squareup.okhttp3:okhttp</b> coordinates and
     * with version reference <b>okhttp</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getOkhttp() {
        return create("okhttp");
    }

    /**
     * Dependency provider for <b>okio</b> with <b>com.squareup.okio:okio</b> coordinates and
     * with version reference <b>okio</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getOkio() {
        return create("okio");
    }

    /**
     * Dependency provider for <b>roomCompiler</b> with <b>androidx.room:room-compiler</b> coordinates and
     * with version reference <b>room</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getRoomCompiler() {
        return create("roomCompiler");
    }

    /**
     * Dependency provider for <b>roomKtx</b> with <b>androidx.room:room-ktx</b> coordinates and
     * with version reference <b>room</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getRoomKtx() {
        return create("roomKtx");
    }

    /**
     * Dependency provider for <b>roomRuntime</b> with <b>androidx.room:room-runtime</b> coordinates and
     * with version reference <b>room</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getRoomRuntime() {
        return create("roomRuntime");
    }

    /**
     * Dependency provider for <b>roomTesting</b> with <b>androidx.room:room-testing</b> coordinates and
     * with version reference <b>room</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getRoomTesting() {
        return create("roomTesting");
    }

    /**
     * Dependency provider for <b>sardine</b> with <b>com.github.thegrizzlylabs:sardine-android</b> coordinates and
     * with version reference <b>sardine</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getSardine() {
        return create("sardine");
    }

    /**
     * Dependency provider for <b>webkit</b> with <b>androidx.webkit:webkit</b> coordinates and
     * with version reference <b>webkit</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getWebkit() {
        return create("webkit");
    }

    /**
     * Group of libraries at <b>about</b>
     */
    public AboutLibraryAccessors getAbout() {
        return laccForAboutLibraryAccessors;
    }

    /**
     * Group of libraries at <b>accompanist</b>
     */
    public AccompanistLibraryAccessors getAccompanist() {
        return laccForAccompanistLibraryAccessors;
    }

    /**
     * Group of libraries at <b>activity</b>
     */
    public ActivityLibraryAccessors getActivity() {
        return laccForActivityLibraryAccessors;
    }

    /**
     * Group of libraries at <b>android</b>
     */
    public AndroidLibraryAccessors getAndroid() {
        return laccForAndroidLibraryAccessors;
    }

    /**
     * Group of libraries at <b>apache</b>
     */
    public ApacheLibraryAccessors getApache() {
        return laccForApacheLibraryAccessors;
    }

    /**
     * Group of libraries at <b>code</b>
     */
    public CodeLibraryAccessors getCode() {
        return laccForCodeLibraryAccessors;
    }

    /**
     * Group of libraries at <b>coil</b>
     */
    public CoilLibraryAccessors getCoil() {
        return laccForCoilLibraryAccessors;
    }

    /**
     * Group of libraries at <b>compose</b>
     */
    public ComposeLibraryAccessors getCompose() {
        return laccForComposeLibraryAccessors;
    }

    /**
     * Group of libraries at <b>constraintlayout</b>
     */
    public ConstraintlayoutLibraryAccessors getConstraintlayout() {
        return laccForConstraintlayoutLibraryAccessors;
    }

    /**
     * Group of libraries at <b>espresso</b>
     */
    public EspressoLibraryAccessors getEspresso() {
        return laccForEspressoLibraryAccessors;
    }

    /**
     * Group of libraries at <b>ext</b>
     */
    public ExtLibraryAccessors getExt() {
        return laccForExtLibraryAccessors;
    }

    /**
     * Group of libraries at <b>floatingx</b>
     */
    public FloatingxLibraryAccessors getFloatingx() {
        return laccForFloatingxLibraryAccessors;
    }

    /**
     * Group of libraries at <b>hutool</b>
     */
    public HutoolLibraryAccessors getHutool() {
        return laccForHutoolLibraryAccessors;
    }

    /**
     * Group of libraries at <b>kotlin</b>
     */
    public KotlinLibraryAccessors getKotlin() {
        return laccForKotlinLibraryAccessors;
    }

    /**
     * Group of libraries at <b>kotlinx</b>
     */
    public KotlinxLibraryAccessors getKotlinx() {
        return laccForKotlinxLibraryAccessors;
    }

    /**
     * Group of libraries at <b>ktor</b>
     */
    public KtorLibraryAccessors getKtor() {
        return laccForKtorLibraryAccessors;
    }

    /**
     * Group of libraries at <b>lifecycle</b>
     */
    public LifecycleLibraryAccessors getLifecycle() {
        return laccForLifecycleLibraryAccessors;
    }

    /**
     * Group of libraries at <b>logback</b>
     */
    public LogbackLibraryAccessors getLogback() {
        return laccForLogbackLibraryAccessors;
    }

    /**
     * Group of libraries at <b>markwon</b>
     */
    public MarkwonLibraryAccessors getMarkwon() {
        return laccForMarkwonLibraryAccessors;
    }

    /**
     * Group of libraries at <b>media3</b>
     */
    public Media3LibraryAccessors getMedia3() {
        return laccForMedia3LibraryAccessors;
    }

    /**
     * Group of libraries at <b>mozilla</b>
     */
    public MozillaLibraryAccessors getMozilla() {
        return laccForMozillaLibraryAccessors;
    }

    /**
     * Group of libraries at <b>navigation</b>
     */
    public NavigationLibraryAccessors getNavigation() {
        return laccForNavigationLibraryAccessors;
    }

    /**
     * Group of libraries at <b>slf4j</b>
     */
    public Slf4jLibraryAccessors getSlf4j() {
        return laccForSlf4jLibraryAccessors;
    }

    /**
     * Group of libraries at <b>splitties</b>
     */
    public SplittiesLibraryAccessors getSplitties() {
        return laccForSplittiesLibraryAccessors;
    }

    /**
     * Group of versions at <b>versions</b>
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Group of bundles at <b>bundles</b>
     */
    public BundleAccessors getBundles() {
        return baccForBundleAccessors;
    }

    /**
     * Group of plugins at <b>plugins</b>
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class AboutLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public AboutLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>about</b> with <b>com.mikepenz:aboutlibraries-core</b> coordinates and
         * with version reference <b>about</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("about");
        }

        /**
         * Dependency provider for <b>compose</b> with <b>com.mikepenz:aboutlibraries-compose</b> coordinates and
         * with version reference <b>about</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCompose() {
            return create("about.compose");
        }

    }

    public static class AccompanistLibraryAccessors extends SubDependencyFactory {
        private final AccompanistNativationLibraryAccessors laccForAccompanistNativationLibraryAccessors = new AccompanistNativationLibraryAccessors(owner);

        public AccompanistLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>permissions</b> with <b>com.google.accompanist:accompanist-permissions</b> coordinates and
         * with version reference <b>accompanist</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getPermissions() {
            return create("accompanist.permissions");
        }

        /**
         * Dependency provider for <b>systemuicontroller</b> with <b>com.google.accompanist:accompanist-systemuicontroller</b> coordinates and
         * with version reference <b>accompanist</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSystemuicontroller() {
            return create("accompanist.systemuicontroller");
        }

        /**
         * Dependency provider for <b>webview</b> with <b>com.google.accompanist:accompanist-webview</b> coordinates and
         * with version reference <b>accompanist</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getWebview() {
            return create("accompanist.webview");
        }

        /**
         * Group of libraries at <b>accompanist.nativation</b>
         */
        public AccompanistNativationLibraryAccessors getNativation() {
            return laccForAccompanistNativationLibraryAccessors;
        }

    }

    public static class AccompanistNativationLibraryAccessors extends SubDependencyFactory {

        public AccompanistNativationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>animation</b> with <b>com.google.accompanist:accompanist-navigation-animation</b> coordinates and
         * with version reference <b>accompanist</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAnimation() {
            return create("accompanist.nativation.animation");
        }

    }

    public static class ActivityLibraryAccessors extends SubDependencyFactory {

        public ActivityLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>compose</b> with <b>androidx.activity:activity-compose</b> coordinates and
         * with version reference <b>activityKtx</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCompose() {
            return create("activity.compose");
        }

    }

    public static class AndroidLibraryAccessors extends SubDependencyFactory {
        private final AndroidToolsLibraryAccessors laccForAndroidToolsLibraryAccessors = new AndroidToolsLibraryAccessors(owner);

        public AndroidLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>gradlePlugin</b> with <b>com.android.tools.build:gradle</b> coordinates and
         * with version reference <b>agp</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getGradlePlugin() {
            return create("android.gradlePlugin");
        }

        /**
         * Dependency provider for <b>svg</b> with <b>com.caverock:androidsvg</b> coordinates and
         * with version reference <b>androidsgv</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSvg() {
            return create("android.svg");
        }

        /**
         * Group of libraries at <b>android.tools</b>
         */
        public AndroidToolsLibraryAccessors getTools() {
            return laccForAndroidToolsLibraryAccessors;
        }

    }

    public static class AndroidToolsLibraryAccessors extends SubDependencyFactory {

        public AndroidToolsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>common</b> with <b>com.android.tools:common</b> coordinates and
         * with version reference <b>androidTools</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCommon() {
            return create("android.tools.common");
        }

    }

    public static class ApacheLibraryAccessors extends SubDependencyFactory {
        private final ApacheCommonsLibraryAccessors laccForApacheCommonsLibraryAccessors = new ApacheCommonsLibraryAccessors(owner);

        public ApacheLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>apache.commons</b>
         */
        public ApacheCommonsLibraryAccessors getCommons() {
            return laccForApacheCommonsLibraryAccessors;
        }

    }

    public static class ApacheCommonsLibraryAccessors extends SubDependencyFactory {

        public ApacheCommonsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>text</b> with <b>org.apache.commons:commons-text</b> coordinates and
         * with version reference <b>apache.commons.text</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getText() {
            return create("apache.commons.text");
        }

    }

    public static class CodeLibraryAccessors extends SubDependencyFactory {
        private final CodeEditorLibraryAccessors laccForCodeEditorLibraryAccessors = new CodeEditorLibraryAccessors(owner);

        public CodeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>code.editor</b>
         */
        public CodeEditorLibraryAccessors getEditor() {
            return laccForCodeEditorLibraryAccessors;
        }

    }

    public static class CodeEditorLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public CodeEditorLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>editor</b> with <b>io.github.Rosemoe.sora-editor:editor</b> coordinates and
         * with version reference <b>codeEditor</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("code.editor");
        }

        /**
         * Dependency provider for <b>textmate</b> with <b>io.github.Rosemoe.sora-editor:language-textmate</b> coordinates and
         * with version reference <b>codeEditor</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getTextmate() {
            return create("code.editor.textmate");
        }

    }

    public static class CoilLibraryAccessors extends SubDependencyFactory {

        public CoilLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>compose</b> with <b>io.coil-kt.coil3:coil-compose</b> coordinates and
         * with version reference <b>coil</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCompose() {
            return create("coil.compose");
        }

        /**
         * Dependency provider for <b>okhttp</b> with <b>io.coil-kt.coil3:coil-network-okhttp</b> coordinates and
         * with version reference <b>coil</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getOkhttp() {
            return create("coil.okhttp");
        }

    }

    public static class ComposeLibraryAccessors extends SubDependencyFactory {
        private final ComposeMaterialLibraryAccessors laccForComposeMaterialLibraryAccessors = new ComposeMaterialLibraryAccessors(owner);
        private final ComposeMaterial3LibraryAccessors laccForComposeMaterial3LibraryAccessors = new ComposeMaterial3LibraryAccessors(owner);
        private final ComposeUiLibraryAccessors laccForComposeUiLibraryAccessors = new ComposeUiLibraryAccessors(owner);

        public ComposeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>bom</b> with <b>androidx.compose:compose-bom</b> coordinates and
         * with version reference <b>composeBom</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getBom() {
            return create("compose.bom");
        }

        /**
         * Dependency provider for <b>datasaver</b> with <b>com.github.FunnySaltyFish.ComposeDataSaver:data-saver</b> coordinates and
         * with version reference <b>dataSaver</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getDatasaver() {
            return create("compose.datasaver");
        }

        /**
         * Dependency provider for <b>foundation</b> with <b>androidx.compose.foundation:foundation</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getFoundation() {
            return create("compose.foundation");
        }

        /**
         * Dependency provider for <b>reorderable</b> with <b>org.burnoutcrew.composereorderable:reorderable</b> coordinates and
         * with version reference <b>reorderable</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getReorderable() {
            return create("compose.reorderable");
        }

        /**
         * Group of libraries at <b>compose.material</b>
         */
        public ComposeMaterialLibraryAccessors getMaterial() {
            return laccForComposeMaterialLibraryAccessors;
        }

        /**
         * Group of libraries at <b>compose.material3</b>
         */
        public ComposeMaterial3LibraryAccessors getMaterial3() {
            return laccForComposeMaterial3LibraryAccessors;
        }

        /**
         * Group of libraries at <b>compose.ui</b>
         */
        public ComposeUiLibraryAccessors getUi() {
            return laccForComposeUiLibraryAccessors;
        }

    }

    public static class ComposeMaterialLibraryAccessors extends SubDependencyFactory {
        private final ComposeMaterialIconsLibraryAccessors laccForComposeMaterialIconsLibraryAccessors = new ComposeMaterialIconsLibraryAccessors(owner);

        public ComposeMaterialLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>compose.material.icons</b>
         */
        public ComposeMaterialIconsLibraryAccessors getIcons() {
            return laccForComposeMaterialIconsLibraryAccessors;
        }

    }

    public static class ComposeMaterialIconsLibraryAccessors extends SubDependencyFactory {

        public ComposeMaterialIconsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>core</b> with <b>androidx.compose.material:material-icons-core</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("compose.material.icons.core");
        }

        /**
         * Dependency provider for <b>extended</b> with <b>androidx.compose.material:material-icons-extended</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getExtended() {
            return create("compose.material.icons.extended");
        }

    }

    public static class ComposeMaterial3LibraryAccessors extends SubDependencyFactory {

        public ComposeMaterial3LibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>core</b> with <b>androidx.compose.material3:material3</b> coordinates and
         * with version reference <b>material3</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("compose.material3.core");
        }

        /**
         * Dependency provider for <b>winsize</b> with <b>androidx.compose.material3:material3-window-size-class</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getWinsize() {
            return create("compose.material3.winsize");
        }

    }

    public static class ComposeUiLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public ComposeUiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>ui</b> with <b>androidx.compose.ui:ui</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("compose.ui");
        }

        /**
         * Dependency provider for <b>android</b> with <b>androidx.compose.ui:ui-android</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAndroid() {
            return create("compose.ui.android");
        }

    }

    public static class ConstraintlayoutLibraryAccessors extends SubDependencyFactory {

        public ConstraintlayoutLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>compose</b> with <b>androidx.constraintlayout:constraintlayout-compose</b> coordinates and
         * with version reference <b>constraintlayoutCompose</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCompose() {
            return create("constraintlayout.compose");
        }

    }

    public static class EspressoLibraryAccessors extends SubDependencyFactory {

        public EspressoLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>core</b> with <b>androidx.test.espresso:espresso-core</b> coordinates and
         * with version reference <b>espressoCore</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("espresso.core");
        }

    }

    public static class ExtLibraryAccessors extends SubDependencyFactory {

        public ExtLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>junit</b> with <b>androidx.test.ext:junit</b> coordinates and
         * with version reference <b>junitVersion</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJunit() {
            return create("ext.junit");
        }

    }

    public static class FloatingxLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public FloatingxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>floatingx</b> with <b>io.github.petterpx:floatingx</b> coordinates and
         * with version reference <b>floatingx</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("floatingx");
        }

        /**
         * Dependency provider for <b>compsoe</b> with <b>io.github.petterpx:floatingx-compose</b> coordinates and
         * with version reference <b>floatingx</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCompsoe() {
            return create("floatingx.compsoe");
        }

    }

    public static class HutoolLibraryAccessors extends SubDependencyFactory {

        public HutoolLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>cache</b> with <b>cn.hutool:hutool-cache</b> coordinates and
         * with version reference <b>hutool</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCache() {
            return create("hutool.cache");
        }

        /**
         * Dependency provider for <b>crypto</b> with <b>cn.hutool:hutool-crypto</b> coordinates and
         * with version reference <b>hutool</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCrypto() {
            return create("hutool.crypto");
        }

    }

    public static class KotlinLibraryAccessors extends SubDependencyFactory {

        public KotlinLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>gradlePlugin</b> with <b>org.jetbrains.kotlin:kotlin-gradle-plugin</b> coordinates and
         * with version reference <b>kotlin</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getGradlePlugin() {
            return create("kotlin.gradlePlugin");
        }

        /**
         * Dependency provider for <b>result</b> with <b>com.michael-bull.kotlin-result:kotlin-result</b> coordinates and
         * with version reference <b>kotlin.result</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getResult() {
            return create("kotlin.result");
        }

    }

    public static class KotlinxLibraryAccessors extends SubDependencyFactory {
        private final KotlinxCoroutinesLibraryAccessors laccForKotlinxCoroutinesLibraryAccessors = new KotlinxCoroutinesLibraryAccessors(owner);
        private final KotlinxSerializationLibraryAccessors laccForKotlinxSerializationLibraryAccessors = new KotlinxSerializationLibraryAccessors(owner);

        public KotlinxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>kotlinx.coroutines</b>
         */
        public KotlinxCoroutinesLibraryAccessors getCoroutines() {
            return laccForKotlinxCoroutinesLibraryAccessors;
        }

        /**
         * Group of libraries at <b>kotlinx.serialization</b>
         */
        public KotlinxSerializationLibraryAccessors getSerialization() {
            return laccForKotlinxSerializationLibraryAccessors;
        }

    }

    public static class KotlinxCoroutinesLibraryAccessors extends SubDependencyFactory {

        public KotlinxCoroutinesLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>android</b> with <b>org.jetbrains.kotlinx:kotlinx-coroutines-android</b> coordinates and
         * with version reference <b>coroutines</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAndroid() {
            return create("kotlinx.coroutines.android");
        }

        /**
         * Dependency provider for <b>core</b> with <b>org.jetbrains.kotlinx:kotlinx-coroutines-core</b> coordinates and
         * with version reference <b>coroutines</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("kotlinx.coroutines.core");
        }

    }

    public static class KotlinxSerializationLibraryAccessors extends SubDependencyFactory {

        public KotlinxSerializationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>json</b> with <b>org.jetbrains.kotlinx:kotlinx-serialization-json</b> coordinates and
         * with version reference <b>serializationJson</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJson() {
            return create("kotlinx.serialization.json");
        }

    }

    public static class KtorLibraryAccessors extends SubDependencyFactory {
        private final KtorServerLibraryAccessors laccForKtorServerLibraryAccessors = new KtorServerLibraryAccessors(owner);

        public KtorLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>ktor.server</b>
         */
        public KtorServerLibraryAccessors getServer() {
            return laccForKtorServerLibraryAccessors;
        }

    }

    public static class KtorServerLibraryAccessors extends SubDependencyFactory {
        private final KtorServerContentLibraryAccessors laccForKtorServerContentLibraryAccessors = new KtorServerContentLibraryAccessors(owner);
        private final KtorServerSerializationLibraryAccessors laccForKtorServerSerializationLibraryAccessors = new KtorServerSerializationLibraryAccessors(owner);
        private final KtorServerStatusLibraryAccessors laccForKtorServerStatusLibraryAccessors = new KtorServerStatusLibraryAccessors(owner);

        public KtorServerLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>core</b> with <b>io.ktor:ktor-server-core</b> coordinates and
         * with version reference <b>ktor.server</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("ktor.server.core");
        }

        /**
         * Dependency provider for <b>cors</b> with <b>io.ktor:ktor-server-cors</b> coordinates and
         * with version reference <b>ktor.server</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCors() {
            return create("ktor.server.cors");
        }

        /**
         * Dependency provider for <b>netty</b> with <b>io.ktor:ktor-server-netty</b> coordinates and
         * with version reference <b>ktor.server</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getNetty() {
            return create("ktor.server.netty");
        }

        /**
         * Group of libraries at <b>ktor.server.content</b>
         */
        public KtorServerContentLibraryAccessors getContent() {
            return laccForKtorServerContentLibraryAccessors;
        }

        /**
         * Group of libraries at <b>ktor.server.serialization</b>
         */
        public KtorServerSerializationLibraryAccessors getSerialization() {
            return laccForKtorServerSerializationLibraryAccessors;
        }

        /**
         * Group of libraries at <b>ktor.server.status</b>
         */
        public KtorServerStatusLibraryAccessors getStatus() {
            return laccForKtorServerStatusLibraryAccessors;
        }

    }

    public static class KtorServerContentLibraryAccessors extends SubDependencyFactory {

        public KtorServerContentLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>negotiation</b> with <b>io.ktor:ktor-server-content-negotiation</b> coordinates and
         * with version reference <b>ktor.server</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getNegotiation() {
            return create("ktor.server.content.negotiation");
        }

    }

    public static class KtorServerSerializationLibraryAccessors extends SubDependencyFactory {

        public KtorServerSerializationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>json</b> with <b>io.ktor:ktor-serialization-kotlinx-json</b> coordinates and
         * with version reference <b>ktor.server</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJson() {
            return create("ktor.server.serialization.json");
        }

    }

    public static class KtorServerStatusLibraryAccessors extends SubDependencyFactory {

        public KtorServerStatusLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>pages</b> with <b>io.ktor:ktor-server-status-pages</b> coordinates and
         * with version reference <b>ktor.server</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getPages() {
            return create("ktor.server.status.pages");
        }

    }

    public static class LifecycleLibraryAccessors extends SubDependencyFactory {
        private final LifecycleLivedataLibraryAccessors laccForLifecycleLivedataLibraryAccessors = new LifecycleLivedataLibraryAccessors(owner);
        private final LifecycleRuntimeLibraryAccessors laccForLifecycleRuntimeLibraryAccessors = new LifecycleRuntimeLibraryAccessors(owner);
        private final LifecycleViewmodelLibraryAccessors laccForLifecycleViewmodelLibraryAccessors = new LifecycleViewmodelLibraryAccessors(owner);

        public LifecycleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>lifecycle.livedata</b>
         */
        public LifecycleLivedataLibraryAccessors getLivedata() {
            return laccForLifecycleLivedataLibraryAccessors;
        }

        /**
         * Group of libraries at <b>lifecycle.runtime</b>
         */
        public LifecycleRuntimeLibraryAccessors getRuntime() {
            return laccForLifecycleRuntimeLibraryAccessors;
        }

        /**
         * Group of libraries at <b>lifecycle.viewmodel</b>
         */
        public LifecycleViewmodelLibraryAccessors getViewmodel() {
            return laccForLifecycleViewmodelLibraryAccessors;
        }

    }

    public static class LifecycleLivedataLibraryAccessors extends SubDependencyFactory {

        public LifecycleLivedataLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>ktx</b> with <b>androidx.lifecycle:lifecycle-livedata-ktx</b> coordinates and
         * with version reference <b>lifecyle</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getKtx() {
            return create("lifecycle.livedata.ktx");
        }

    }

    public static class LifecycleRuntimeLibraryAccessors extends SubDependencyFactory {

        public LifecycleRuntimeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>compose</b> with <b>androidx.lifecycle:lifecycle-runtime-compose</b> coordinates and
         * with version reference <b>lifecyle</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCompose() {
            return create("lifecycle.runtime.compose");
        }

    }

    public static class LifecycleViewmodelLibraryAccessors extends SubDependencyFactory {

        public LifecycleViewmodelLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>ktx</b> with <b>androidx.lifecycle:lifecycle-viewmodel-ktx</b> coordinates and
         * with version reference <b>lifecyle</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getKtx() {
            return create("lifecycle.viewmodel.ktx");
        }

    }

    public static class LogbackLibraryAccessors extends SubDependencyFactory {

        public LogbackLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>android</b> with <b>com.github.tony19:logback-android</b> coordinates and
         * with version reference <b>logback.android</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAndroid() {
            return create("logback.android");
        }

        /**
         * Dependency provider for <b>classic</b> with <b>ch.qos.logback:logback-classic</b> coordinates and
         * with version reference <b>logback</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getClassic() {
            return create("logback.classic");
        }

    }

    public static class MarkwonLibraryAccessors extends SubDependencyFactory {

        public MarkwonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>core</b> with <b>io.noties.markwon:core</b> coordinates and
         * with version reference <b>markwon</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("markwon.core");
        }

        /**
         * Dependency provider for <b>image</b> with <b>io.noties.markwon:image</b> coordinates and
         * with version reference <b>markwon</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getImage() {
            return create("markwon.image");
        }

        /**
         * Dependency provider for <b>linkify</b> with <b>io.noties.markwon:linkify</b> coordinates and
         * with version reference <b>markwon</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getLinkify() {
            return create("markwon.linkify");
        }

    }

    public static class Media3LibraryAccessors extends SubDependencyFactory {

        public Media3LibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>exo</b> with <b>androidx.media3:media3-exoplayer</b> coordinates and
         * with version reference <b>media</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getExo() {
            return create("media3.exo");
        }

        /**
         * Dependency provider for <b>ui</b> with <b>androidx.media3:media3-ui</b> coordinates and
         * with version reference <b>media</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getUi() {
            return create("media3.ui");
        }

    }

    public static class MozillaLibraryAccessors extends SubDependencyFactory {

        public MozillaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>rhino</b> with <b>org.mozilla:rhino</b> coordinates and
         * with version reference <b>rhino</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getRhino() {
            return create("mozilla.rhino");
        }

    }

    public static class NavigationLibraryAccessors extends SubDependencyFactory {

        public NavigationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>compose</b> with <b>androidx.navigation:navigation-compose</b> coordinates and
         * with version reference <b>navigationCompose</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCompose() {
            return create("navigation.compose");
        }

    }

    public static class Slf4jLibraryAccessors extends SubDependencyFactory {

        public Slf4jLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>api</b> with <b>org.slf4j:slf4j-api</b> coordinates and
         * with version reference <b>slf4j</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getApi() {
            return create("slf4j.api");
        }

        /**
         * Dependency provider for <b>simple</b> with <b>org.slf4j:slf4j-simple</b> coordinates and
         * with version reference <b>slf4j</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSimple() {
            return create("slf4j.simple");
        }

    }

    public static class SplittiesLibraryAccessors extends SubDependencyFactory {

        public SplittiesLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>appctx</b> with <b>com.louiscad.splitties:splitties-appctx</b> coordinates and
         * with version reference <b>splitties</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAppctx() {
            return create("splitties.appctx");
        }

        /**
         * Dependency provider for <b>systemservices</b> with <b>com.louiscad.splitties:splitties-systemservices</b> coordinates and
         * with version reference <b>splitties</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSystemservices() {
            return create("splitties.systemservices");
        }

    }

    public static class VersionAccessors extends VersionFactory  {

        private final ApacheVersionAccessors vaccForApacheVersionAccessors = new ApacheVersionAccessors(providers, config);
        private final KotlinVersionAccessors vaccForKotlinVersionAccessors = new KotlinVersionAccessors(providers, config);
        private final KtorVersionAccessors vaccForKtorVersionAccessors = new KtorVersionAccessors(providers, config);
        private final LogbackVersionAccessors vaccForLogbackVersionAccessors = new LogbackVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>about</b> with value <b>10.9.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAbout() { return getVersion("about"); }

        /**
         * Version alias <b>accompanist</b> with value <b>0.33.0-alpha</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAccompanist() { return getVersion("accompanist"); }

        /**
         * Version alias <b>activityKtx</b> with value <b>1.10.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getActivityKtx() { return getVersion("activityKtx"); }

        /**
         * Version alias <b>agp</b> with value <b>8.8.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAgp() { return getVersion("agp"); }

        /**
         * Version alias <b>androidTools</b> with value <b>31.8.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAndroidTools() { return getVersion("androidTools"); }

        /**
         * Version alias <b>androidsgv</b> with value <b>1.4</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAndroidsgv() { return getVersion("androidsgv"); }

        /**
         * Version alias <b>appcompat</b> with value <b>1.7.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAppcompat() { return getVersion("appcompat"); }

        /**
         * Version alias <b>codeEditor</b> with value <b>0.23.5</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCodeEditor() { return getVersion("codeEditor"); }

        /**
         * Version alias <b>coil</b> with value <b>3.1.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCoil() { return getVersion("coil"); }

        /**
         * Version alias <b>compileSdk</b> with value <b>35</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCompileSdk() { return getVersion("compileSdk"); }

        /**
         * Version alias <b>composeBom</b> with value <b>2025.02.00</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getComposeBom() { return getVersion("composeBom"); }

        /**
         * Version alias <b>constraintlayoutCompose</b> with value <b>1.1.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getConstraintlayoutCompose() { return getVersion("constraintlayoutCompose"); }

        /**
         * Version alias <b>coreKtx</b> with value <b>1.15.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCoreKtx() { return getVersion("coreKtx"); }

        /**
         * Version alias <b>coroutines</b> with value <b>1.10.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCoroutines() { return getVersion("coroutines"); }

        /**
         * Version alias <b>dataSaver</b> with value <b>v1.1.5</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getDataSaver() { return getVersion("dataSaver"); }

        /**
         * Version alias <b>desugar</b> with value <b>2.1.5</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getDesugar() { return getVersion("desugar"); }

        /**
         * Version alias <b>espressoCore</b> with value <b>3.6.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getEspressoCore() { return getVersion("espressoCore"); }

        /**
         * Version alias <b>floatingx</b> with value <b>2.3.4</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getFloatingx() { return getVersion("floatingx"); }

        /**
         * Version alias <b>hanlp</b> with value <b>portable-1.8.6</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getHanlp() { return getVersion("hanlp"); }

        /**
         * Version alias <b>hutool</b> with value <b>5.8.19</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getHutool() { return getVersion("hutool"); }

        /**
         * Version alias <b>junit</b> with value <b>4.13.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJunit() { return getVersion("junit"); }

        /**
         * Version alias <b>junitVersion</b> with value <b>1.2.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJunitVersion() { return getVersion("junitVersion"); }

        /**
         * Version alias <b>ksp</b> with value <b>2.1.10-1.0.29</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getKsp() { return getVersion("ksp"); }

        /**
         * Version alias <b>lifecyle</b> with value <b>2.8.7</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getLifecyle() { return getVersion("lifecyle"); }

        /**
         * Version alias <b>logging</b> with value <b>7.0.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getLogging() { return getVersion("logging"); }

        /**
         * Version alias <b>markwon</b> with value <b>4.6.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getMarkwon() { return getVersion("markwon"); }

        /**
         * Version alias <b>material3</b> with value <b>1.4.0-alpha09</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getMaterial3() { return getVersion("material3"); }

        /**
         * Version alias <b>media</b> with value <b>1.5.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getMedia() { return getVersion("media"); }

        /**
         * Version alias <b>minSdk</b> with value <b>21</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getMinSdk() { return getVersion("minSdk"); }

        /**
         * Version alias <b>navigationCompose</b> with value <b>2.8.8</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getNavigationCompose() { return getVersion("navigationCompose"); }

        /**
         * Version alias <b>net</b> with value <b>3.7.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getNet() { return getVersion("net"); }

        /**
         * Version alias <b>okhttp</b> with value <b>4.12.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getOkhttp() { return getVersion("okhttp"); }

        /**
         * Version alias <b>okio</b> with value <b>3.6.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getOkio() { return getVersion("okio"); }

        /**
         * Version alias <b>reorderable</b> with value <b>0.9.6</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getReorderable() { return getVersion("reorderable"); }

        /**
         * Version alias <b>rhino</b> with value <b>1.8.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getRhino() { return getVersion("rhino"); }

        /**
         * Version alias <b>room</b> with value <b>2.6.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getRoom() { return getVersion("room"); }

        /**
         * Version alias <b>sardine</b> with value <b>v0.8</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getSardine() { return getVersion("sardine"); }

        /**
         * Version alias <b>serializationJson</b> with value <b>1.7.3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getSerializationJson() { return getVersion("serializationJson"); }

        /**
         * Version alias <b>slf4j</b> with value <b>2.0.16</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getSlf4j() { return getVersion("slf4j"); }

        /**
         * Version alias <b>splitties</b> with value <b>3.0.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getSplitties() { return getVersion("splitties"); }

        /**
         * Version alias <b>targetSdk</b> with value <b>35</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getTargetSdk() { return getVersion("targetSdk"); }

        /**
         * Version alias <b>webkit</b> with value <b>1.12.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getWebkit() { return getVersion("webkit"); }

        /**
         * Group of versions at <b>versions.apache</b>
         */
        public ApacheVersionAccessors getApache() {
            return vaccForApacheVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.kotlin</b>
         */
        public KotlinVersionAccessors getKotlin() {
            return vaccForKotlinVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.ktor</b>
         */
        public KtorVersionAccessors getKtor() {
            return vaccForKtorVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.logback</b>
         */
        public LogbackVersionAccessors getLogback() {
            return vaccForLogbackVersionAccessors;
        }

    }

    public static class ApacheVersionAccessors extends VersionFactory  {

        private final ApacheCommonsVersionAccessors vaccForApacheCommonsVersionAccessors = new ApacheCommonsVersionAccessors(providers, config);
        public ApacheVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.apache.commons</b>
         */
        public ApacheCommonsVersionAccessors getCommons() {
            return vaccForApacheCommonsVersionAccessors;
        }

    }

    public static class ApacheCommonsVersionAccessors extends VersionFactory  {

        public ApacheCommonsVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>apache.commons.text</b> with value <b>1.13.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getText() { return getVersion("apache.commons.text"); }

    }

    public static class KotlinVersionAccessors extends VersionFactory  implements VersionNotationSupplier {

        public KotlinVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>kotlin</b> with value <b>2.1.10</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> asProvider() { return getVersion("kotlin"); }

        /**
         * Version alias <b>kotlin.result</b> with value <b>2.0.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getResult() { return getVersion("kotlin.result"); }

    }

    public static class KtorVersionAccessors extends VersionFactory  {

        public KtorVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>ktor.server</b> with value <b>3.0.3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getServer() { return getVersion("ktor.server"); }

    }

    public static class LogbackVersionAccessors extends VersionFactory  implements VersionNotationSupplier {

        public LogbackVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>logback</b> with value <b>1.5.7</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> asProvider() { return getVersion("logback"); }

        /**
         * Version alias <b>logback.android</b> with value <b>3.0.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAndroid() { return getVersion("logback.android"); }

    }

    public static class BundleAccessors extends BundleFactory {
        private final AndoridBundleAccessors baccForAndoridBundleAccessors = new AndoridBundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
        private final ComposeBundleAccessors baccForComposeBundleAccessors = new ComposeBundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

        /**
         * Dependency bundle provider for <b>about</b> which contains the following dependencies:
         * <ul>
         *    <li>com.mikepenz:aboutlibraries-core</li>
         *    <li>com.mikepenz:aboutlibraries-compose</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> getAbout() {
            return createBundle("about");
        }

        /**
         * Dependency bundle provider for <b>accompanist</b> which contains the following dependencies:
         * <ul>
         *    <li>com.google.accompanist:accompanist-systemuicontroller</li>
         *    <li>com.google.accompanist:accompanist-navigation-animation</li>
         *    <li>com.google.accompanist:accompanist-webview</li>
         *    <li>com.google.accompanist:accompanist-permissions</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> getAccompanist() {
            return createBundle("accompanist");
        }

        /**
         * Dependency bundle provider for <b>codeEditor</b> which contains the following dependencies:
         * <ul>
         *    <li>io.github.Rosemoe.sora-editor:editor</li>
         *    <li>io.github.Rosemoe.sora-editor:language-textmate</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> getCodeEditor() {
            return createBundle("codeEditor");
        }

        /**
         * Dependency bundle provider for <b>coil</b> which contains the following dependencies:
         * <ul>
         *    <li>io.coil-kt.coil3:coil-compose</li>
         *    <li>io.coil-kt.coil3:coil-network-okhttp</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> getCoil() {
            return createBundle("coil");
        }

        /**
         * Dependency bundle provider for <b>ktor</b> which contains the following dependencies:
         * <ul>
         *    <li>io.ktor:ktor-server-netty</li>
         *    <li>io.ktor:ktor-server-core</li>
         *    <li>io.ktor:ktor-server-content-negotiation</li>
         *    <li>io.ktor:ktor-serialization-kotlinx-json</li>
         *    <li>io.ktor:ktor-server-cors</li>
         *    <li>io.ktor:ktor-server-status-pages</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> getKtor() {
            return createBundle("ktor");
        }

        /**
         * Dependency bundle provider for <b>lifecycle</b> which contains the following dependencies:
         * <ul>
         *    <li>androidx.lifecycle:lifecycle-runtime-compose</li>
         *    <li>androidx.lifecycle:lifecycle-viewmodel-ktx</li>
         *    <li>androidx.lifecycle:lifecycle-livedata-ktx</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> getLifecycle() {
            return createBundle("lifecycle");
        }

        /**
         * Dependency bundle provider for <b>markwon</b> which contains the following dependencies:
         * <ul>
         *    <li>io.noties.markwon:core</li>
         *    <li>io.noties.markwon:image</li>
         *    <li>io.noties.markwon:linkify</li>
         *    <li>com.caverock:androidsvg</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> getMarkwon() {
            return createBundle("markwon");
        }

        /**
         * Dependency bundle provider for <b>media3</b> which contains the following dependencies:
         * <ul>
         *    <li>androidx.media3:media3-exoplayer</li>
         *    <li>androidx.media3:media3-ui</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> getMedia3() {
            return createBundle("media3");
        }

        /**
         * Dependency bundle provider for <b>network</b> which contains the following dependencies:
         * <ul>
         *    <li>com.squareup.okio:okio</li>
         *    <li>com.squareup.okhttp3:okhttp</li>
         *    <li>com.github.liangjingkanji:Net</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> getNetwork() {
            return createBundle("network");
        }

        /**
         * Dependency bundle provider for <b>room</b> which contains the following dependencies:
         * <ul>
         *    <li>androidx.room:room-runtime</li>
         *    <li>androidx.room:room-ktx</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> getRoom() {
            return createBundle("room");
        }

        /**
         * Group of bundles at <b>bundles.andorid</b>
         */
        public AndoridBundleAccessors getAndorid() {
            return baccForAndoridBundleAccessors;
        }

        /**
         * Group of bundles at <b>bundles.compose</b>
         */
        public ComposeBundleAccessors getCompose() {
            return baccForComposeBundleAccessors;
        }

    }

    public static class AndoridBundleAccessors extends BundleFactory {

        public AndoridBundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

        /**
         * Dependency bundle provider for <b>andorid.base</b> which contains the following dependencies:
         * <ul>
         *    <li>androidx.core:core-ktx</li>
         *    <li>androidx.appcompat:appcompat</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> getBase() {
            return createBundle("andorid.base");
        }

    }

    public static class ComposeBundleAccessors extends BundleFactory  implements BundleNotationSupplier{

        public ComposeBundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

        /**
         * Dependency bundle provider for <b>compose</b> which contains the following dependencies:
         * <ul>
         *    <li>androidx.compose.foundation:foundation</li>
         *    <li>androidx.compose.ui:ui</li>
         *    <li>androidx.activity:activity-compose</li>
         *    <li>androidx.constraintlayout:constraintlayout-compose</li>
         *    <li>androidx.navigation:navigation-compose</li>
         *    <li>org.burnoutcrew.composereorderable:reorderable</li>
         *    <li>com.github.FunnySaltyFish.ComposeDataSaver:data-saver</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> asProvider() {
            return createBundle("compose");
        }

        /**
         * Dependency bundle provider for <b>compose.floatingx</b> which contains the following dependencies:
         * <ul>
         *    <li>io.github.petterpx:floatingx</li>
         *    <li>io.github.petterpx:floatingx-compose</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> getFloatingx() {
            return createBundle("compose.floatingx");
        }

        /**
         * Dependency bundle provider for <b>compose.material3</b> which contains the following dependencies:
         * <ul>
         *    <li>androidx.compose.material:material-icons-core</li>
         *    <li>androidx.compose.material:material-icons-extended</li>
         *    <li>androidx.compose.material3:material3-window-size-class</li>
         *    <li>androidx.compose.material3:material3</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> getMaterial3() {
            return createBundle("compose.material3");
        }

    }

    public static class PluginAccessors extends PluginFactory {
        private final AndroidPluginAccessors paccForAndroidPluginAccessors = new AndroidPluginAccessors(providers, config);
        private final ComposePluginAccessors paccForComposePluginAccessors = new ComposePluginAccessors(providers, config);
        private final KotlinPluginAccessors paccForKotlinPluginAccessors = new KotlinPluginAccessors(providers, config);

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Plugin provider for <b>about</b> with plugin id <b>com.mikepenz.aboutlibraries.plugin</b> and
         * with version reference <b>about</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getAbout() { return createPlugin("about"); }

        /**
         * Plugin provider for <b>androidApplication</b> with plugin id <b>com.github.jing332.android.application</b> and
         * with version <b>unspecified</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getAndroidApplication() { return createPlugin("androidApplication"); }

        /**
         * Plugin provider for <b>application</b> with plugin id <b>com.android.application</b> and
         * with version reference <b>agp</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getApplication() { return createPlugin("application"); }

        /**
         * Plugin provider for <b>ksp</b> with plugin id <b>com.google.devtools.ksp</b> and
         * with version reference <b>ksp</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getKsp() { return createPlugin("ksp"); }

        /**
         * Group of plugins at <b>plugins.android</b>
         */
        public AndroidPluginAccessors getAndroid() {
            return paccForAndroidPluginAccessors;
        }

        /**
         * Group of plugins at <b>plugins.compose</b>
         */
        public ComposePluginAccessors getCompose() {
            return paccForComposePluginAccessors;
        }

        /**
         * Group of plugins at <b>plugins.kotlin</b>
         */
        public KotlinPluginAccessors getKotlin() {
            return paccForKotlinPluginAccessors;
        }

    }

    public static class AndroidPluginAccessors extends PluginFactory {

        public AndroidPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Plugin provider for <b>android.library</b> with plugin id <b>com.android.library</b> and
         * with version reference <b>agp</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getLibrary() { return createPlugin("android.library"); }

    }

    public static class ComposePluginAccessors extends PluginFactory {

        public ComposePluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Plugin provider for <b>compose.compiler</b> with plugin id <b>org.jetbrains.kotlin.plugin.compose</b> and
         * with version reference <b>kotlin</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getCompiler() { return createPlugin("compose.compiler"); }

    }

    public static class KotlinPluginAccessors extends PluginFactory {

        public KotlinPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Plugin provider for <b>kotlin.android</b> with plugin id <b>org.jetbrains.kotlin.android</b> and
         * with version reference <b>kotlin</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getAndroid() { return createPlugin("kotlin.android"); }

        /**
         * Plugin provider for <b>kotlin.parcelize</b> with plugin id <b>org.jetbrains.kotlin.plugin.parcelize</b> and
         * with version reference <b>kotlin</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getParcelize() { return createPlugin("kotlin.parcelize"); }

        /**
         * Plugin provider for <b>kotlin.serialization</b> with plugin id <b>org.jetbrains.kotlin.plugin.serialization</b> and
         * with version reference <b>kotlin</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getSerialization() { return createPlugin("kotlin.serialization"); }

    }

}
