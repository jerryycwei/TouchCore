package ca.mcgill.sel.ram.ui.examples;

import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import ca.mcgill.sel.commons.emf.util.AdapterFactoryRegistry;
import ca.mcgill.sel.commons.emf.util.ResourceManager;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.core.provider.CoreItemProviderAdapterFactory;
import ca.mcgill.sel.core.util.CoreResourceFactoryImpl;
import ca.mcgill.sel.ram.RamPackage;
import ca.mcgill.sel.ram.loaders.RamClassLoader;
import ca.mcgill.sel.ram.provider.RamItemProviderAdapterFactory;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.scenes.DisplayUCMScene;
import ca.mcgill.sel.ram.ui.utils.Constants;
import ca.mcgill.sel.ram.ui.utils.ResourceUtils;
import ca.mcgill.sel.ram.ui.utils.ResourceUtils.OperatingSystem;
import ca.mcgill.sel.ram.ui.utils.Strings;
import ca.mcgill.sel.ram.util.RamResourceFactoryImpl;
import ca.mcgill.sel.ucm.UCMPackage;
import ca.mcgill.sel.ucm.UseCaseMap;
import ca.mcgill.sel.ucm.provider.UCMItemProviderAdapterFactory;
import ca.mcgill.sel.ucm.util.UCMResourceFactoryImpl;

/**
 * Javadoc.
 *
 * @author jerrywei
 *
 */
public final class UCMExample {

    /**
     * Javadoc.
     *
     * @author jerrywei
     *
     */
    private UCMExample() {
        // Temporary workaround to rename the dock icon name in Mac OS X.
        // Once the application is bundled as an .app this is not required anymore.
        // Might only work in Java 6 from Apple.
        if (ResourceUtils.getOperatingSystem() == OperatingSystem.OSX) {
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", Strings.APP_NAME);
        }

        // Initialize ResourceManager (will initialize OCL).
        // See issue #84.
        ResourceManager.initialize();

        // Initialize packages.
        RamPackage.eINSTANCE.eClass();
        CorePackage.eINSTANCE.eClass();

        // // Register resource factories.
        ResourceManager.registerExtensionFactory(Constants.ASPECT_FILE_EXTENSION, new RamResourceFactoryImpl());
        ResourceManager.registerExtensionFactory(Constants.CORE_FILE_EXTENSION, new CoreResourceFactoryImpl());
        ResourceManager.registerExtensionFactory(Constants.JUCM_FILE_EXTENSION, new XMIResourceFactoryImpl());
        ResourceManager.registerExtensionFactory(Constants.UCM_FILE_EXTENSION, new UCMResourceFactoryImpl());
        //
        // // Initialize adapter factories.a
        AdapterFactoryRegistry.INSTANCE.addAdapterFactory(RamItemProviderAdapterFactory.class);
        AdapterFactoryRegistry.INSTANCE.addAdapterFactory(CoreItemProviderAdapterFactory.class);
        AdapterFactoryRegistry.INSTANCE.addAdapterFactory(UCMItemProviderAdapterFactory.class);

        ResourceUtils.loadLibraries();
        // // Start the application, the rest will happen by itself.
        //
        RamClassLoader.INSTANCE.initializeWithJavaClasses();

        // if (ResourceUtils.getOperatingSystem() == OperatingSystem.OSX) {
        // System.setProperty("com.apple.mrj.application.apple.menu.about.name", Strings.APP_NAME);
        // }
        //
        // ResourceUtils.loadLibraries();

        RamApp.initialize(new Runnable() {

            @Override
            public void run() {

                UCMPackage packageInstance = UCMPackage.eINSTANCE;

                UseCaseMap useCaseMap =
                        (UseCaseMap) ResourceManager.loadModel(Constants.DIRECTORY_MODELS
                                + "/testFiles/ucmFiles/exampleFile.ucm");

                useCaseMap.setName("exampleFile");

                RamApp app = RamApp.getApplication();
                app.changeScene(new DisplayUCMScene(app, "UcmScene", useCaseMap, true));
            }
        });
    }

    /**
     * Starts the example.
     *
     * @param args
     *            unused.
     */
    public static void main(final String[] args) {
        new UCMExample();
    }

}