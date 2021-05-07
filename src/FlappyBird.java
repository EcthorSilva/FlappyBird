import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements Jogo{
	
	public Passaro passaro;
	public Random gerador = new Random();
	
	public int record = 0;
	public ScoreNumber scorenumber;
	
	public int game_state = 0; //[0->Start Screen] [1->Get Ready] [2->Game] [3->Game Over]
	
	public double ground_offset = 0;
	public ArrayList<Cano> canos = new ArrayList<Cano>();
	public Timer timer_cano;
	public Hitbox groundbox;
	
	public Timer auxtimer;
	
	// add cano
	private Acao addCano(){
		return new Acao(){
			public void executa() {
				canos.add(new Cano (getLargura(), gerador.nextInt(getAltura()-112-Cano.holesize)));
			}
		};
	}
	
	private Acao proxCena(){
		return new Acao(){
			public void executa(){
				game_state += 1;
				game_state = game_state%4;
			}
		};
	}
	
	public FlappyBird() {
		passaro = new Passaro(35, (getLargura() -112)/2);
		timer_cano = new Timer(2, true,  addCano());
		scorenumber = new ScoreNumber(0);
		groundbox = new Hitbox(0, getAltura()-112, getLargura(), getAltura());
	}
	
	
	public String getTitulo() {
		return "Flappy Bird";
	}
	public int getLargura() {
		return 384;
	}
	public int getAltura() {
		return 512;
	}
	
	public void gameOver(){
		canos = new ArrayList<Cano>();
		passaro = new Passaro(50,getAltura()/4);
		proxCena().executa();
	}
	
	public void tique(java.util.Set<String> teclas, double dt) {
		ground_offset += dt*100;
		ground_offset = ground_offset %308;
		
		switch(game_state){
		case 0: //Tela principal
			break;
		case 1: //Get Ready
			auxtimer.tique(dt);
			passaro.updateSprite(dt);
			break;
		case 2: // Tela do jogo
			timer_cano.tique(dt);
			passaro.atualiza(dt);
			passaro.updateSprite(dt);
			if(groundbox.intersecao(passaro.box)!=0){
				gameOver();
				return;
			}
			if(passaro.y<-5){
				gameOver();
				return;
			}
			for(Cano cano: canos){
				cano.atualiza(dt);
				if(cano.boxcima.intersecao(passaro.box)!=0 || cano.boxbaixo.intersecao(passaro.box)!=0){
					if(scorenumber.getScore()>ScoreNumber.record){
						ScoreNumber.record = scorenumber.getScore();
					}
					gameOver();
					return;
				}
				if(!cano.counted && cano.x < passaro.x){
					cano.counted = true;
					scorenumber.modifyScore(1);;
				}
			}
			if(canos.size() > 0 && canos.get(0).x < -70){
				canos.remove(0);
			}
			
			break;
		case 3: //Game Over
			break;
		}
    }
	
	public void tecla(String tecla){
		switch(game_state){
		case 0:
			if(tecla.equals(" ")){
				auxtimer = new Timer(1.6, false, proxCena());
				proxCena().executa();
			}
			break;
		case 1:
			break;
		case 2:
			if(tecla.equals(" ")){
				passaro.flap();
			}
			break;
		case 3:
			if(passaro.equals(" ")){
				scorenumber.setScore(0);
				proxCena().executa();
			}
			break;
		}
    }
	
	public void desenhar(Tela t){
		
		// Background 
		t.imagem("flappy.png", 0, 0, 288, 512, 0, 0, 0);
    	t.imagem("flappy.png", 0, 0, 288, 512, 0, 288, 0);
		
    	
    	// cano
    	for(Cano cano: canos) {
    		cano.drawItself(t);
    	}
    	
		// Ground
		t.imagem("flappy.png", 292, 0, 308, 112, 0, -ground_offset, getAltura()-112);
		t.imagem("flappy.png", 292, 0, 308, 112, 0, 308 -ground_offset, getAltura()-112);
		t.imagem("flappy.png", 292, 0, 308, 112, 0, 308*2 -ground_offset, getAltura()-112);
		
		switch(game_state){
		case 0:
			t.imagem("flappy.png", 292, 346, 192, 44, 0, getLargura()/2 - 192/2, 100);
			t.imagem("flappy.png", 352, 306, 70, 36, 0, getLargura()/2 - 70/2, 175);
			t.texto("Pressione espaço", 60, getAltura()/2 - 16, 32, Cor.BRANCO);
			break;
		case 1:
			passaro.drawItself(t);
			t.imagem("flappy.png",292,442,174,44, 0, getLargura()/2 - 174/2, getAltura()/3);
			scorenumber.drawScore(t, 5, 5);
			break;
		case 2:
			scorenumber.drawScore(t, 5, 5);
			passaro.drawItself(t);
			break;
		case 3:
			t.imagem("flappy.png", 292, 398, 188, 38, 0, getLargura()/2 - 188/2, 100);
			t.imagem("flappy.png", 292, 116, 226, 116, 0, getLargura()/2 - 226/2, getAltura()/2 - 116/2);
			scorenumber.drawScore(t, getLargura()/2 + 50, getAltura()/2-25);
			scorenumber.drawRecord(t, getLargura()/2 + 55, getAltura()/2 + 16);
			break;
		}
		
    }
	
	public static void main (String[] args) {
		roda();
	}
	
	private static void roda() {
		new Motor (new FlappyBird());
	}

}