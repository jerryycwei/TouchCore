package ca.mcgill.sel.ram.ui.utils;

import org.mt4j.sceneManagement.transition.SlideTransition;

import ca.mcgill.sel.core.weaver.COREWeaver;
import ca.mcgill.sel.core.weaver.util.WeaverListener;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.Instantiation;
import ca.mcgill.sel.ram.InstantiationType;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamPopup;
import ca.mcgill.sel.ram.ui.components.RamPopup.PopupType;
import ca.mcgill.sel.ram.weaver.RAMWeaver;

/**
 * The awesome WeaverRunner.
 * 
 * @author mschoettle
 * @author oalam
 */
public final class WeaverRunner implements WeaverListener<Aspect> {

    /**
     * The mode that should be used to weave.
     */
    public enum WeaveMode {
        /**
         * All declared instantiations will be woven into the aspect.
         */
        ALL,

        /**
         * Only one instantiation will be woven into the aspect.
         */
        SINGLE,

        /**
         * Apply CSP to the state machines of all the state views of an aspect.
         */
        STATE_MACHINES,

        /**
         * Weave all without applying the CSP composition to the state machines.
         */
        ALL_NO_CSP;

    };

    private static WeaverRunner instance;

    private RamPopup weavePopup;
    private Instantiation instantiation;

    /**
     * Prevent instantiation.
     */
    private WeaverRunner() {
    }

    /**
     * Get the singleton instance of WeaverRunner.
     *
     * @return the instance.
     */
    public static WeaverRunner getInstance() {
        if (instance == null) {
            instance = new WeaverRunner();
        }
        return instance;
    }

    /**
     * Runs the weaver in the given mode.
     *
     * @param weaveMode mode to use for weaving.
     */
    private void runWeaver(WeaveMode weaveMode) {
        runWeaver(weaveMode, null);
    }

    /**
     * Runs the weaver in the given mode.
     *
     * @param weaveMode mode to use for weaving.
     * @param currentInstantiation {@link Instantiation} to weave (only used in WeaveMode.SINGLE)
     */
    private void runWeaver(final WeaveMode weaveMode, final Instantiation currentInstantiation) {
        final Aspect base = RamApp.getActiveAspectScene().getAspect();
        base.eSetDeliver(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // calling the java weaver
                RAMWeaver weaver = RAMWeaver.getInstance();
                COREWeaver cweaver = COREWeaver.getInstance();
                switch (weaveMode) {
                    case ALL:
                        cweaver.weaveAll(base, WeaverRunner.this);
                        break;
                    case SINGLE:
                        if (currentInstantiation.getType() == InstantiationType.EXTENDS) {
                            cweaver.weaveExtendSingle(base, instantiation, WeaverRunner.this);
                        } else {
                            cweaver.weaveModelReuseSingle(base, instantiation, WeaverRunner.this);
                        }
                        break;
                    case STATE_MACHINES:
                        weaver.weaveStateMachines(base, WeaverRunner.this);
                        break;
                    case ALL_NO_CSP:
                        weaver.weaveAllNoCSPWeavingForStateViews(base, WeaverRunner.this);
                        break;
                }
            }
        }).start();

    }

    /**
     * Weave all depending aspects into the current one.
     */
    public void weaveAll() {
        runWeaver(WeaveMode.ALL);
    }

    /**
     * Weave only a single aspect into the current one.
     * 
     * @param currentInstantiation
     *            the instantiation of the aspect that should be woven into the current one
     */
    public void weaveSingle(Instantiation currentInstantiation) {
        if (currentInstantiation != null) {
            this.instantiation = currentInstantiation;
            runWeaver(WeaveMode.SINGLE, currentInstantiation);
        }
    }

    /**
     * Apply CSP composition to the state machines of a state view.
     */
    public void weaveStateMachines() {
        runWeaver(WeaveMode.STATE_MACHINES);
    }

    /**
     * Weave all without applying CSP to the state views.
     */
    public void weaveAllNoCSPForStateViews() {
        runWeaver(WeaveMode.ALL_NO_CSP);
    }

    @Override
    public void weavingStarted() {
        if (weavePopup != null) {
            weavePopup.destroy();
        }
        RamApp.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                weavePopup = new RamPopup(Strings.POPUP_WEAVING, true);
                RamApp.getActiveAspectScene().displayPopup(weavePopup);
            }
        });

    }

    @Override
    public void weavingFinished(final Aspect result) {
        if (result == null) {
            finalizeWeaving();
            return;
        }
        // temporarily rename the aspect
        if (instantiation != null) {
            String externalAspectName = instantiation.getSource().getName();
            String aspectName = result.getName().concat("_").concat(externalAspectName);
            result.setName(aspectName);
        } else {
            result.setName("Woven_".concat(result.getName()));
        }
        // display the aspect to the user; transition to the left
        RamApp.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                finalizeWeaving();
                RamApp.getActiveAspectScene().setTransition(new SlideTransition(RamApp.getApplication(), 700, true));
                RamApp.getApplication().loadAspect(result);
            }
        });
    }

    @Override
    public void weavingFailed(Exception e) {
        // Get error message
        final StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Strings.POPUP_ERROR_WEAVING);

        if (e != null && e.getLocalizedMessage() != null) {
            stringBuffer.append(" " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        RamApp.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                finalizeWeaving();
                RamApp.getActiveAspectScene().displayPopup(stringBuffer.toString(), PopupType.ERROR);
            }
        });

    }

    /**
     * Destroy the "weaving..." popup and reactivate notifications for aspect that was woven.
     */
    private void finalizeWeaving() {
        Aspect base = RamApp.getActiveAspectScene().getAspect();
        base.eSetDeliver(true);
        if (weavePopup != null) {
            weavePopup.destroy();
            weavePopup = null;
        }
    }

}
