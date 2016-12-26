package org.diylc.swing.plugins.tree;

import java.util.EnumSet;

import javax.swing.SwingConstants;

import org.apache.log4j.Logger;
import org.diylc.appframework.miscutils.ConfigurationManager;
import org.diylc.appframework.miscutils.IConfigListener;
import org.diylc.common.BadPositionException;
import org.diylc.common.EventType;
import org.diylc.common.IPlugIn;
import org.diylc.common.IPlugInPort;
import org.diylc.swing.ISwingUI;
import org.diylc.swing.plugins.config.ConfigPlugin;
import org.diylc.swing.plugins.statusbar.StatusBar;


public class ComponentTree implements IPlugIn {

  private static final Logger LOG = Logger.getLogger(StatusBar.class);

  private ISwingUI swingUI;
  private IPlugInPort plugInPort;

  private TreePanel treePanel;

  public ComponentTree(ISwingUI swingUI) {
    this.swingUI = swingUI;
  }

  @Override
  public void connect(IPlugInPort plugInPort) {
    this.plugInPort = plugInPort;
    try {
      swingUI.injectGUIComponent(getTreePanel(), SwingConstants.LEFT);
    } catch (BadPositionException e) {
      LOG.error("Could not install the component tree", e);
    }
    ConfigurationManager.getInstance().addConfigListener(ConfigPlugin.COMPONENT_BROWSER, new IConfigListener() {

      @Override
      public void valueChanged(String key, Object value) {
        getTreePanel().setVisible(
            ConfigPlugin.COMPONENT_BROWSER.equals(key) && ConfigPlugin.SEARCHABLE_TREE.equals(value));
      }
    });

    getTreePanel().setVisible(
        ConfigurationManager.getInstance().readString(ConfigPlugin.COMPONENT_BROWSER, ConfigPlugin.SEARCHABLE_TREE)
            .equals(ConfigPlugin.SEARCHABLE_TREE));
  }

  public TreePanel getTreePanel() {
    if (treePanel == null) {
      treePanel = new TreePanel(plugInPort);
    }
    return treePanel;
  }

  @Override
  public EnumSet<EventType> getSubscribedEventTypes() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void processMessage(EventType eventType, Object... params) {
    // TODO Auto-generated method stub

  }
}