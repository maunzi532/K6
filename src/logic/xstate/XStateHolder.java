package logic.xstate;

public interface XStateHolder
{
	void setState(NState state);

	NState getState();
}