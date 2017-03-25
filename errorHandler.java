public void nonFatalError(Exception e, Token t, String expr){
    int offset = t.offset();
  	System.out.println("Non-fatal error encountered at offset" + offset);
  	System.out.println(expr);
  	for(int i=0; i < (offset-1); i++){
  	  System.out.println("-");
  	  }
  	System.out.println("^");
  	if(verbose == true){
  		System.out.println(e.stackTrace);
  	}
}
