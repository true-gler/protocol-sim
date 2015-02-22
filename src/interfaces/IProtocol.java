package interfaces;

import model.Node;
import model.Paket;
import event.Event;

public interface IProtocol {
		
	public void executeTX(Object... object);
	public void executeRX(Object... object);
	public void executePreSimulation(Object... object);
	public void executeFinished(Object... object);
	public void executePostSimulation(Object... object);
}
