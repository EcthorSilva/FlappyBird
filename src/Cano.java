public class Cano {
	
	public double x, y;
	
	public static int xspeed = -100;
	public static int holesize = 110;
	public boolean counted = false;
	
	public Hitbox boxcima;
	public Hitbox boxbaixo;
	
	public Cano(double x, double y) {
		this.x = x;
		this.y = y;
		
		boxcima = new Hitbox(x, y-270, x+52, y);
		boxbaixo = new Hitbox(x, y+Cano.holesize, x+52, y+Cano.holesize+242);
	}
	
	public void atualiza (double dt) {
		x += xspeed*dt;
		
		boxcima.mover(xspeed*dt, 0);
		boxbaixo.mover(xspeed*dt, 0);
	}
	
	public void drawItself (Tela t) {
		
		// cao de cima 
		t.imagem("flappy.png", 604, 0, 52, 270, 0, x, y-270);
		t.imagem("flappy.png", 660, 42, 52, 200, 0, x, y + holesize + 242); //resto do pipe virado pra cima
		
		// cao de baixo 
		t.imagem("flappy.png", 660, 0, 52, 242, 0, x, y+Cano.holesize);
		t.imagem("flappy.png", 604, 0, 52, 220, 0, x, y - 270 - 220); //resto do pipe virado pra baixo
	}
}
