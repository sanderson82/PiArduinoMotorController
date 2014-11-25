package sa.server;

public enum ServerCommand {
	STEERING_SERVO(3),
	LEFT_RIGHT_CAMERA_SERVO(10),
	UP_DOWN_CAMERA_SERVO(9),
	FORWARD_MOTOR(5),
	REVERSE_MOTOR(6);
	
	private int value;

	private ServerCommand(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return value;
	}
}
