package homework;

public class MyPoint2D {
	private float x;
	private float y;
	
	public MyPoint2D() { y = 0; x = 0; }
	public MyPoint2D(float _x, float _y) { y = _y; x = _x; }
	public void set(float _x, float _y) { x =_x; y = _y; }
	public void setX(float _x) { x =_x; }
	public void setY(float _y) { y =_y; }
	public float getX() { return x; }
	public float getY() { return y; }
	public void add(float _x, float _y) { x += _x; y += _y; }
	public void subt(float _x, float _y) { x -= _x; y -= _y; }
}
