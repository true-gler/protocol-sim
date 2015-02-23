package interfaces;

import data.Paket;
import model.Node;
import event.Event;

public interface IProtocol {
		
	public void executeTX(Object... object);
	public void executeRX(Object... object);
	public void executePreSimulation(Object... object);
	public void executeFinished(Object... object);
	public void executePostSimulation(Object... object);
}
