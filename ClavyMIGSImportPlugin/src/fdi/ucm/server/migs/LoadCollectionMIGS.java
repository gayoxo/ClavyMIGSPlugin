/**
 * 
 */
package fdi.ucm.server.migs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;



import fdi.ucm.server.modelComplete.ImportExportDataEnum;
import fdi.ucm.server.modelComplete.ImportExportPair;
import fdi.ucm.server.modelComplete.LoadCollection;
import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.CompleteCollectionAndLog;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class LoadCollectionMIGS extends LoadCollection{

	
	private CompleteCollection CC;
	private ArrayList<String> Logs;
//	private CompleteLinkElementType topicIDTC;
	public static boolean consoleDebug=false;
	private ArrayList<ImportExportPair> Parametros;
	private HashMap<Integer, CompleteDocuments> ObjetoVirtual;
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LoadCollectionMIGS LC=new LoadCollectionMIGS();
		LoadCollectionMIGS.consoleDebug=true;
		
		ArrayList<String> Lista = new ArrayList<String>();
		Lista.add("localhost");
		Lista.add("migs");
		Lista.add("3306");
		Lista.add("root");
		Lista.add("");
		
		CompleteCollectionAndLog Salida=LC.processCollecccion(Lista);
		if (Salida!=null)
			{
			
			System.out.println("Correcto");
			
			for (String warning : Salida.getLogLines())
				System.err.println(warning);

			
			System.exit(0);
			
			}
		else
			{
			System.err.println("Error");
			System.exit(-1);
			}
	}

	

	@Override
	public CompleteCollectionAndLog processCollecccion(ArrayList<String> dateEntrada) {
		try {
			CompleteCollectionAndLog Salida=new CompleteCollectionAndLog();
			CC=new CompleteCollection("MIGS", new Date()+"");
			Salida.setCollection(CC);
			Logs=new ArrayList<String>();
			Salida.setLogLines(Logs);

			process_Objetos_Digitales();
			
			
			return Salida;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	


	private void process_Objetos_Digitales() {
		ObjetoVirtual=new HashMap<Integer, CompleteDocuments>();
		
		CompleteGrammar GrammarVO = new CompleteGrammar(NameConstantsMIGS.VIRTUAL_OBJECTNAME, NameConstantsMIGS.VIRTUAL_OBJECTNAME,CC);
		
		{
		String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
		
		CompleteOperationalValueType Valor = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		GrammarVO.getViews().add(Valor);
		GrammarVO.getViews().add(Valor2);
		GrammarVO.getViews().add(Valor3);
		

		String VistaOVMeta=new String(NameConstantsMIGS.META);
		
		CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsMIGS.TYPE,NameConstantsMIGS.VIRTUAL_OBJECT,VistaOVMeta);
		
		GrammarVO.getViews().add(ValorMeta);
		
		
		String VistaOVOda=new String(NameConstantsMIGS.ODA);
		
		CompleteOperationalValueType ValorOda=new CompleteOperationalValueType(NameConstantsMIGS.PUBLIC,Boolean.toString(true),VistaOVOda);

		CompleteOperationalValueType ValorOda2=new CompleteOperationalValueType(NameConstantsMIGS.PRIVATE,Boolean.toString(false),VistaOVOda);

		GrammarVO.getViews().add(ValorOda);
		GrammarVO.getViews().add(ValorOda2);
		}
		
		
		CC.getMetamodelGrammar().add(GrammarVO);
		
		CompleteTextElementType idovattr=new CompleteTextElementType(NameConstantsMIGS.IDOV,GrammarVO);
		
		{
		String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
		
		CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		idovattr.getShows().add(VisibleAtt);
		idovattr.getShows().add(Valor2);
		idovattr.getShows().add(Valor3);
		
		String VistaOVMeta=new String(NameConstantsMIGS.META);
		
		CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsMIGS.TYPE,NameConstantsMIGS.IDOV,VistaOVMeta);
		
		idovattr.getShows().add(ValorMeta);
		
		String VistaMetaType=new String(NameConstantsMIGS.METATYPE);
		CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsMIGS.METATYPETYPE,NameConstantsMIGS.TEXT,VistaMetaType);
		idovattr.getShows().add(MetaType);
		}
		
		
CompleteTextElementType ovType=new CompleteTextElementType(NameConstantsMIGS.OVType,GrammarVO);
		
		{
		String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
		
		CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		ovType.getShows().add(VisibleAtt);
		ovType.getShows().add(Valor2);
		ovType.getShows().add(Valor3);
		
		String VistaOVMeta=new String(NameConstantsMIGS.META);
		
		CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsMIGS.TYPE,NameConstantsMIGS.OVType,VistaOVMeta);
		
		ovType.getShows().add(ValorMeta);
		
		String VistaMetaType=new String(NameConstantsMIGS.METATYPE);
		CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsMIGS.METATYPETYPE,NameConstantsMIGS.TEXT,VistaMetaType);
		ovType.getShows().add(MetaType);
		}
		
	}



	@Override
	public ArrayList<ImportExportPair> getConfiguracion() {
		if (Parametros==null)
		{
			ArrayList<ImportExportPair> ListaCampos=new ArrayList<ImportExportPair>();
			ListaCampos.add(new ImportExportPair(ImportExportDataEnum.Text, "Server"));
			ListaCampos.add(new ImportExportPair(ImportExportDataEnum.Text, "Database"));
			ListaCampos.add(new ImportExportPair(ImportExportDataEnum.Number, "Port"));
			ListaCampos.add(new ImportExportPair(ImportExportDataEnum.Text, "User"));
			ListaCampos.add(new ImportExportPair(ImportExportDataEnum.EncriptedText, "Password"));
			Parametros=ListaCampos;
			return ListaCampos;
		}
		else return Parametros;
	}

	@Override
	public String getName() {
		return "MIGS";
	}

	@Override
	public boolean getCloneLocalFiles() {
		return false;
	}

}
