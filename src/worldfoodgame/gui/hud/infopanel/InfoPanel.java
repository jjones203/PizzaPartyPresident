package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.gui.WorldPresenter;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by winston on 3/21/15.
 */
public class InfoPanel implements Observer
{
  private final WorldPresenter worldPresenter;
  private final static Dimension SIZE = new Dimension(1, 224);
  private TabbedPanel OuterTabbedPanel;
  private TabbedPanel InnerTabbedPanel;

  public InfoPanel(WorldPresenter worldPresenter)
  {
    this.worldPresenter = worldPresenter;
    worldPresenter.addObserver(this);

  }

  @Override
  public void update(Observable o, Object arg)
  {

  }
}
