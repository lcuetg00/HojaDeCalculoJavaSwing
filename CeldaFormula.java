public class CeldaFormula implements Celda{
	
	String formula;
	HojaDeCalculo hojaDeCalculo;

	public CeldaFormula(String formula, HojaDeCalculo hojaDeCalculo) {
		this.formula=formula;
		this.hojaDeCalculo=hojaDeCalculo;
	}

	
	@Override
	public int getValue() {
		return processString(formula); 
	}
	

	@Override
	public String toString() {
		return "="+formula;
	}
	
	private int processString(String s) {
		if(s.indexOf('+')!=-1) {
			return processString(s.substring(0,s.indexOf('+'))) + 
					processString(s.substring(s.indexOf('+')+1,s.length()));
		} else if(s.indexOf('-')!=-1) {
			return processString(s.substring(0,s.indexOf('-'))) -
			processString(s.substring(s.indexOf('-')+1,s.length()));
			
		} else if(s.indexOf('/')!=-1) {
			return (int)(processString(s.substring(0,s.indexOf('/'))) / 
					processString(s.substring(s.indexOf('/')+1,s.length())));
					
		} else if(s.indexOf('*')!=-1) {
			return processString(s.substring(0,s.indexOf('*'))) * 
					processString(s.substring(s.indexOf('*')+1,s.length()));
			
		} else if(esConstante(s)) {
			return Integer.parseInt(s);
			
		} else if(esReferenciaCelda(s)) {
			int[] a=extraerCoordenadasCelda(s);
			return hojaDeCalculo.getValue(a[0],a[1]);
		}
		return 0;
			
	}
	/**
	 * El método esConstante comprueba si el String argumentado es un número. Si no lo es devuelve falso.
	 * @param s
	 * @return
	 */
	public static boolean esConstante(String s) { 
		for(int i=0;i<s.length();i++) {
			if(s.charAt(i)<'0' || s.charAt(i)>'9') {
				return false;
			}
		}
		return true;
	}
	
	private static final int NUM_MAX_LETRAS=3;
	private static final int NUM_MAX_DIGITOS=3;
	
	/**
	 * El método esReferenciaCelda comprueba si el String argumentado contiene una referencia 
	 * adecuadamente formateada a otra celda de la tabla. Si no devuelve falso.
	 * @param s
	 * @return
	 */
	public static boolean esReferenciaCelda(String s) { 
		int i=0;
		boolean letraEncontrada=false;
		boolean numEncontrado=false;
		int cuentaLetras=0;
		int cuentaNumeros=0;


			while(i<s.length()) {
				
				if(s.charAt(i)>='A' && s.charAt(i)<='Z') {
					letraEncontrada=true;
					cuentaLetras++;
					i++;
					if(cuentaLetras>NUM_MAX_LETRAS) {
						return false;
					}
				} else {
					break;
				}
				
			}
		while(i<s.length()) {
			if(s.charAt(i)<'0' || s.charAt(i)>'9') {
				return false;
			} else {
				numEncontrado=true;
				cuentaNumeros++;
				i++;
				if(cuentaNumeros>NUM_MAX_DIGITOS) {
					return false;
				}
			}
		}
		return letraEncontrada && numEncontrado;
	}
	
	public static final int NUMLETRAS='Z'-'A'+1;
	/**
	 * Precondición: el String argumentado debe de contener letras seguido de números.
	 * @param s
	 * @return
	 */
	public static int[] extraerCoordenadasCelda(String s) {
		int[] array= new int[2];
		int i=0;
		while(s.charAt(i)>='A' && s.charAt(i)<='Z') { //Busco la parte donde empiezan los dígitos
			i++;
		}
		array[0]= Integer.parseInt(s.substring(i))-1;
		
		for(int j=0;j<i;j++) {
			array[1]+=(s.charAt(j)-'A'+1)*Math.pow(NUMLETRAS, i-j-1);
		}
		array[1]=array[1]-1;
		return array;
	}
	
	