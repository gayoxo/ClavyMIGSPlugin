/**
 * 
 */
package fdi.ucm.server.migs;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fdi.ucm.server.modelComplete.ImportExportDataEnum;
import fdi.ucm.server.modelComplete.ImportExportPair;
import fdi.ucm.server.modelComplete.LoadCollection;
import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.CompleteCollectionAndLog;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteLinkElement;
import fdi.ucm.server.modelComplete.collection.document.CompleteResourceElementURL;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteLinkElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteResourceElementType;
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
	private fdi.ucm.server.migs.MIGSConnectionChasqui MQL;
	
	
	
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
	public CompleteCollectionAndLog processCollecccion(ArrayList<String> DateEntrada) {
		try {
			CompleteCollectionAndLog Salida=new CompleteCollectionAndLog();
			CC=new CompleteCollection("MIGS", new Date()+"");
			Salida.setCollection(CC);
			Logs=new ArrayList<String>();
			Salida.setLogLines(Logs);

			if (DateEntrada!=null)
			{
				String Database = RemoveSpecialCharacters(DateEntrada.get(1));
				MQL=new MIGSConnectionChasqui(DateEntrada.get(0),Database,Integer.parseInt(DateEntrada.get(2)),DateEntrada.get(3),DateEntrada.get(4));

			}
			
			
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
		GrammarVO.getSons().add(idovattr);
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
		GrammarVO.getSons().add(ovType);
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
		
		
		try {
			ResultSet rs=MQL.RunQuerrySELECT("SELECT * FROM objeto_virtual;");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String Dato=rs.getObject("idov").toString();
					String Descripcion=rs.getObject("descripcion").toString();
					String tipo=rs.getObject("tipoOV").toString();
					if (Dato!=null&&!Dato.isEmpty())
						{
						int Idov=Integer.parseInt(Dato);
						CompleteCollection C=CC;
						CompleteDocuments sectionValue = new CompleteDocuments(C,Descripcion,"");
						C.getEstructuras().add(sectionValue);
						ObjetoVirtual.put(Idov, sectionValue);
						
						CompleteTextElement CTE=new CompleteTextElement(idovattr,Dato);
						CompleteTextElement OVE=new CompleteTextElement(ovType,tipo);
						
						sectionValue.getDescription().add(OVE);
						sectionValue.getDescription().add(CTE);
						
						
						
						}
					else System.err.println("ErrorIdovNotFound");
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		CompleteElementType Datos=new CompleteElementType(NameConstantsMIGS.DATOS,GrammarVO);
		GrammarVO.getSons().add(Datos);
		{
		String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
		
		CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		Datos.getShows().add(VisibleAtt);
		Datos.getShows().add(Valor2);
		Datos.getShows().add(Valor3);
		
		String VistaOVMeta=new String(NameConstantsMIGS.META);
		
		CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsMIGS.DATOS,NameConstantsMIGS.OVType,VistaOVMeta);
		
		Datos.getShows().add(ValorMeta);
		}
		
		procesaDatos(Datos,GrammarVO);
		
		
		CompleteElementType Metadatos=new CompleteElementType(NameConstantsMIGS.METADATOS,GrammarVO);
		GrammarVO.getSons().add(Metadatos);
		{
		String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
		
		CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		Metadatos.getShows().add(VisibleAtt);
		Metadatos.getShows().add(Valor2);
		Metadatos.getShows().add(Valor3);
		
		String VistaOVMeta=new String(NameConstantsMIGS.META);
		
		CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsMIGS.METADATOS,NameConstantsMIGS.OVType,VistaOVMeta);
		
		Metadatos.getShows().add(ValorMeta);
		}
		
		procesaMetaDatos(Metadatos,GrammarVO);
		
		CompleteElementType Recursos=new CompleteElementType(NameConstantsMIGS.RESOURCE,GrammarVO);
		GrammarVO.getSons().add(Recursos);
		{
		String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
		
		CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		Recursos.getShows().add(VisibleAtt);
		Recursos.getShows().add(Valor2);
		Recursos.getShows().add(Valor3);
		
		String VistaOVMeta=new String(NameConstantsMIGS.META);
		
		CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsMIGS.RESOURCE,NameConstantsMIGS.OVType,VistaOVMeta);
		
		Recursos.getShows().add(ValorMeta);
		}
		
		procesaRecursos(Recursos,GrammarVO);
	}



	private void procesaRecursos(CompleteElementType recursos, CompleteGrammar grammarVO) {
		
		
List<CompleteResourceElementType> RecursosFilesList=new ArrayList<CompleteResourceElementType>();
		
	CompleteResourceElementType RecursosD=new CompleteResourceElementType(NameConstantsMIGS.RESOURCE+" "+NameConstantsMIGS.URL,recursos,grammarVO);
		recursos.getSons().add(RecursosD);
		RecursosFilesList.add(RecursosD);
		RecursosD.setMultivalued(true);
		{
		String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
		
		CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		RecursosD.getShows().add(VisibleAtt);
		RecursosD.getShows().add(Valor2);
		RecursosD.getShows().add(Valor3);
		}
		
		
		CompleteTextElementType NomRec=new CompleteTextElementType(NameConstantsMIGS.NOM_REC,RecursosD,grammarVO);
		RecursosD.getSons().add(NomRec);
		{
		String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
		
		CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		NomRec.getShows().add(VisibleAtt);
		NomRec.getShows().add(Valor2);
		NomRec.getShows().add(Valor3);
		}
		
		CompleteTextElementType NomRecPub=new CompleteTextElementType(NameConstantsMIGS.NOM_REC_PUBLICO,RecursosD,grammarVO);
		RecursosD.getSons().add(NomRecPub);
		{
		String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
		
		CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		NomRecPub.getShows().add(VisibleAtt);
		NomRecPub.getShows().add(Valor2);
		NomRecPub.getShows().add(Valor3);
		}
		
		CompleteTextElementType TypeRec=new CompleteTextElementType(NameConstantsMIGS.TYPE_REC,RecursosD,grammarVO);
		RecursosD.getSons().add(TypeRec);
		{
		String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
		
		CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		TypeRec.getShows().add(VisibleAtt);
		TypeRec.getShows().add(Valor2);
		TypeRec.getShows().add(Valor3);
		}
		
		CompleteTextElementType DesrecRec=new CompleteTextElementType(NameConstantsMIGS.DESCRIPTION,RecursosD,grammarVO);
		RecursosD.getSons().add(DesrecRec);
		{
		String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
		
		CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		DesrecRec.getShows().add(VisibleAtt);
		DesrecRec.getShows().add(Valor2);
		DesrecRec.getShows().add(Valor3);
		}
		
		
		HashMap<Integer,List<CompleteResourceElementType>> tablaDatList=new HashMap<Integer,List<CompleteResourceElementType>>();
		
		try {
			ResultSet rs=MQL.RunQuerrySELECT("SELECT idov,nom_rec,nom_rec_publico,tipoRec,descripcion,tipo FROM recursos WHERE tipo!='OV' ORDER BY idov;");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String idov=rs.getObject("idov").toString();
					String nom_rec=rs.getObject("nom_rec").toString();
					String nom_rec_publico=rs.getObject("nom_rec_publico").toString();
					String tipoRec=rs.getObject("tipoRec").toString();
					String descripcion=rs.getObject("descripcion").toString();

					if (idov!=null&&!idov.isEmpty()&&nom_rec!=null&&!nom_rec.isEmpty())
						{
						Integer idovL = Integer.parseInt(idov);
						List<CompleteResourceElementType> TTT=tablaDatList.get(idovL);	
						
						if (TTT==null)
							TTT=new ArrayList<CompleteResourceElementType>();
						
						while (TTT.size()>=RecursosFilesList.size())
							{
							CompleteResourceElementType RecursosD2=new CompleteResourceElementType(NameConstantsMIGS.RESOURCE+" "+NameConstantsMIGS.URL,recursos,grammarVO);
							recursos.getSons().add(RecursosD2);
							RecursosFilesList.add(RecursosD2);
							RecursosD2.setMultivalued(true);
							RecursosD2.setClassOfIterator(RecursosD);
							{
							String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
							
							CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
							CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
							CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
							
							RecursosD2.getShows().add(VisibleAtt);
							RecursosD2.getShows().add(Valor2);
							RecursosD2.getShows().add(Valor3);
							}
							
							CompleteTextElementType NomRec2=new CompleteTextElementType(NameConstantsMIGS.NOM_REC,RecursosD2,grammarVO);
							RecursosD2.getSons().add(NomRec2);
							NomRec2.setClassOfIterator(NomRec);
							{
							String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
							
							CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
							CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
							CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
							
							NomRec2.getShows().add(VisibleAtt);
							NomRec2.getShows().add(Valor2);
							NomRec2.getShows().add(Valor3);
							}
							
							CompleteTextElementType NomRecPub2=new CompleteTextElementType(NameConstantsMIGS.NOM_REC_PUBLICO,RecursosD2,grammarVO);
							RecursosD2.getSons().add(NomRecPub2);
							NomRecPub2.setClassOfIterator(NomRecPub);
							{
							String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
							
							CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
							CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
							CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
							
							NomRecPub2.getShows().add(VisibleAtt);
							NomRecPub2.getShows().add(Valor2);
							NomRecPub2.getShows().add(Valor3);
							}
							
							CompleteTextElementType TypeRec2=new CompleteTextElementType(NameConstantsMIGS.TYPE_REC,RecursosD2,grammarVO);
							RecursosD2.getSons().add(TypeRec2);
							TypeRec2.setClassOfIterator(TypeRec);
							{
							String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
							
							CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
							CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
							CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
							
							TypeRec2.getShows().add(VisibleAtt);
							TypeRec2.getShows().add(Valor2);
							TypeRec2.getShows().add(Valor3);
							}
							
							CompleteTextElementType DesrecRec2=new CompleteTextElementType(NameConstantsMIGS.DESCRIPTION,RecursosD2,grammarVO);
							RecursosD2.getSons().add(DesrecRec2);
							DesrecRec2.setClassOfIterator(DesrecRec);
							{
							String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
							
							CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
							CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
							CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
							
							DesrecRec2.getShows().add(VisibleAtt);
							DesrecRec2.getShows().add(Valor2);
							DesrecRec2.getShows().add(Valor3);
							}
							
							

							}
						
						CompleteResourceElementType este=RecursosFilesList.get(TTT.size());
						
						TTT.add(este);
						tablaDatList.put(idovL, TTT);
						
						CompleteDocuments CD=ObjetoVirtual.get(idovL);
						
						CompleteResourceElementURL CTE=new CompleteResourceElementURL(este, File.separator+idov+File.separator+nom_rec);
						CD.getDescription().add(CTE);		
						
						if (este.getSons().get(0) instanceof CompleteTextElementType && este.getSons().get(0).getName().equals(NameConstantsMIGS.NOM_REC))
						{
						CompleteTextElement CTE2=new CompleteTextElement((CompleteTextElementType) este.getSons().get(0), nom_rec);
						CD.getDescription().add(CTE2);	
						}
						
						if (nom_rec_publico!=null&&!nom_rec_publico.isEmpty()&&este.getSons().get(1) instanceof CompleteTextElementType && este.getSons().get(1).getName().equals(NameConstantsMIGS.NOM_REC_PUBLICO))
						{
						CompleteTextElement CTE2=new CompleteTextElement((CompleteTextElementType) este.getSons().get(1), nom_rec_publico);
						CD.getDescription().add(CTE2);	
						}
						
						if (tipoRec!=null&&!tipoRec.isEmpty()&&este.getSons().get(2) instanceof CompleteTextElementType && este.getSons().get(2).getName().equals(NameConstantsMIGS.TYPE_REC))
						{
						CompleteTextElement CTE2=new CompleteTextElement((CompleteTextElementType) este.getSons().get(2), nom_rec_publico);
						CD.getDescription().add(CTE2);	
						}
						
						if (descripcion!=null&&!descripcion.isEmpty()&&este.getSons().get(3) instanceof CompleteTextElementType && este.getSons().get(3).getName().equals(NameConstantsMIGS.DESCRIPTION))
						{
						CompleteTextElement CTE2=new CompleteTextElement((CompleteTextElementType) este.getSons().get(3), nom_rec_publico);
						CD.getDescription().add(CTE2);	
						}
						
						
						
						}
					else System.err.println("contenido vacio en catalogo");
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		List<CompleteTextElementType> LinkFilesList=new ArrayList<CompleteTextElementType>();
		
		CompleteTextElementType LinkD=new CompleteTextElementType(NameConstantsMIGS.RELACION,recursos,grammarVO);
			recursos.getSons().add(LinkD);
			LinkFilesList.add(LinkD);
			LinkD.setMultivalued(true);
			{
			String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
			
			CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
			
			LinkD.getShows().add(VisibleAtt);
			LinkD.getShows().add(Valor2);
			LinkD.getShows().add(Valor3);
			}
			
			
			CompleteLinkElementType LinkDLink=new CompleteLinkElementType(NameConstantsMIGS.RESOURCE+" "+NameConstantsMIGS.LINK,LinkD,grammarVO);
			LinkD.getSons().add(LinkDLink);
			{
			String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
			
			CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
			
			LinkDLink.getShows().add(VisibleAtt);
			LinkDLink.getShows().add(Valor2);
			LinkDLink.getShows().add(Valor3);
			}
			
			CompleteTextElementType NomRecPubL=new CompleteTextElementType(NameConstantsMIGS.NOM_REC_PUBLICO,LinkD,grammarVO);
			LinkD.getSons().add(NomRecPubL);
			{
			String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
			
			CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
			
			NomRecPubL.getShows().add(VisibleAtt);
			NomRecPubL.getShows().add(Valor2);
			NomRecPubL.getShows().add(Valor3);
			}
			
			HashMap<Integer,List<CompleteTextElementType>> tablaDatList3=new HashMap<Integer,List<CompleteTextElementType>>();
			
			try {
				ResultSet rs=MQL.RunQuerrySELECT("SELECT idov,nom_rec,nom_rec_publico,tipoRec,descripcion,tipo,ruta FROM recursos WHERE tipo='OV' ORDER BY idov;");
				if (rs!=null) 
				{
					while (rs.next()) {
						
						String idov=rs.getObject("idov").toString();
						String tipoRec=rs.getObject("tipoRec").toString();
						String ruta=rs.getObject("ruta").toString();
						String nom_rec_publico=rs.getObject("nom_rec_publico").toString();

						if (idov!=null&&!idov.isEmpty()&&tipoRec!=null&&!tipoRec.isEmpty()&&ruta!=null&&!ruta.isEmpty())
							{
							Integer idovL = Integer.parseInt(idov);
							List<CompleteTextElementType> TTT=tablaDatList3.get(idovL);	
							
							if (TTT==null)
								TTT=new ArrayList<CompleteTextElementType>();
							
							while (TTT.size()>=LinkFilesList.size())
								{
								CompleteTextElementType LinkD2=new CompleteTextElementType(NameConstantsMIGS.RELACION,recursos,grammarVO);
								recursos.getSons().add(LinkD2);
								LinkFilesList.add(LinkD2);
								LinkD2.setMultivalued(true);
								LinkD2.setClassOfIterator(LinkD);
								{
								String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
								
								CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
								CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
								CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
								
								LinkD2.getShows().add(VisibleAtt);
								LinkD2.getShows().add(Valor2);
								LinkD2.getShows().add(Valor3);
								}
								
								
								CompleteLinkElementType LinkDLink2=new CompleteLinkElementType(NameConstantsMIGS.RESOURCE+" "+NameConstantsMIGS.LINK,LinkD2,grammarVO);
								LinkD2.getSons().add(LinkDLink2);
								LinkDLink2.setClassOfIterator(LinkDLink);
								{
								String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
								
								CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
								CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
								CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
								
								LinkDLink2.getShows().add(VisibleAtt);
								LinkDLink2.getShows().add(Valor2);
								LinkDLink2.getShows().add(Valor3);
								}

								CompleteTextElementType NomRecPubL2=new CompleteTextElementType(NameConstantsMIGS.NOM_REC_PUBLICO,LinkD2,grammarVO);
								LinkD2.getSons().add(NomRecPubL2);
								NomRecPubL2.setClassOfIterator(NomRecPubL);
								{
								String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
								
								CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
								CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
								CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
								
								NomRecPubL2.getShows().add(VisibleAtt);
								NomRecPubL2.getShows().add(Valor2);
								NomRecPubL2.getShows().add(Valor3);
								}
								
								}
							
							CompleteTextElementType este=LinkFilesList.get(TTT.size());
							
							TTT.add(este);
							tablaDatList3.put(idovL, TTT);
							
							CompleteDocuments CD=ObjetoVirtual.get(idovL);
							
							CompleteTextElement CTE=new CompleteTextElement(este, tipoRec);
							CD.getDescription().add(CTE);		
							
							try {
								CompleteElementType AA=este.getSons().get(0);
								if (AA instanceof CompleteLinkElementType)
								{
									Integer IntLinkD=Integer.parseInt(ruta.substring(0, ruta.length()-1));
									CompleteDocuments CDs=ObjetoVirtual.get(IntLinkD);
								CompleteLinkElement CTEL=new CompleteLinkElement((CompleteLinkElementType)AA, CDs);
								CD.getDescription().add(CTEL);		
								}
							} catch (Exception e) {
								System.err.println("contenido vacio en Link recurso "+ ruta);
							}
							
							if (nom_rec_publico!=null&&!nom_rec_publico.isEmpty()&&este.getSons().get(1) instanceof CompleteTextElementType && este.getSons().get(1).getName().equals(NameConstantsMIGS.NOM_REC_PUBLICO))
							{
							CompleteTextElement CTE2=new CompleteTextElement((CompleteTextElementType) este.getSons().get(1), nom_rec_publico);
							CD.getDescription().add(CTE2);	
							}
							
							
							}
						else System.err.println("contenido vacio en catalogo");
					}
				rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
	}



	private void procesaMetaDatos(CompleteElementType metadatos, CompleteGrammar grammarVO) {
ArrayList<String> Categorias=new ArrayList<String>();
		
		try {
			ResultSet rs=MQL.RunQuerrySELECT("SELECT DISTINCT contenido FROM metadatos WHERE ruta='/manifest/metadata/lom/general/catalogentry/catalog';");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String categoria=rs.getObject("contenido").toString();

					if (categoria!=null&&!categoria.isEmpty())
						{
						Categorias.add(categoria);
						}
					else System.err.println("contenido vacio en catalogo");
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		HashMap<String, CompleteTextElementType> tablaCatalogos=new HashMap<String, CompleteTextElementType>();
		for (String categoria : Categorias) 
			{
			CompleteTextElementType Datos=new CompleteTextElementType(categoria,metadatos,grammarVO);
			metadatos.getSons().add(Datos);
			{
			String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
			
			CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
			
			Datos.getShows().add(VisibleAtt);
			Datos.getShows().add(Valor2);
			Datos.getShows().add(Valor3);
			
			}
			
			tablaCatalogos.put(categoria, Datos);
			
			}
		

		processCatalogsValues(tablaCatalogos);
		
		
Categorias=new ArrayList<String>();
		
		try {
			ResultSet rs=MQL.RunQuerrySELECT("SELECT DISTINCT contenido FROM metadatos WHERE ruta='/manifest/metadata/lom/classification/taxonpath/source/langstring';");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String categoria=rs.getObject("contenido").toString();

					if (categoria!=null&&!categoria.isEmpty())
						{
						Categorias.add(categoria);
						}
					else System.err.println("contenido vacio en taxonomia");
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		HashMap<String, CompleteTextElementType> tablaTaxon=new HashMap<String, CompleteTextElementType>();
		for (String categoria : Categorias) 
			{
			CompleteTextElementType Datos=new CompleteTextElementType(categoria,metadatos,grammarVO);
			metadatos.getSons().add(Datos);
			{
			String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
			
			CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
			
			Datos.getShows().add(VisibleAtt);
			Datos.getShows().add(Valor2);
			Datos.getShows().add(Valor3);
			
			}
			
			tablaTaxon.put(categoria, Datos);
			
			}
		
		processTaxonValues(tablaTaxon);
		
		
		List<CompleteTextElementType> Contribucion=new ArrayList<CompleteTextElementType>();
		
		CompleteTextElementType ContribucionD=new CompleteTextElementType(NameConstantsMIGS.CONTRIBUCION,metadatos,grammarVO);
		metadatos.getSons().add(ContribucionD);
		Contribucion.add(ContribucionD);
		ContribucionD.setMultivalued(true);
		{
		String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
		
		CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		ContribucionD.getShows().add(VisibleAtt);
		ContribucionD.getShows().add(Valor2);
		ContribucionD.getShows().add(Valor3);
		}
		

		CompleteTextElementType EventoD=new CompleteTextElementType(NameConstantsMIGS.EVENTO,ContribucionD,grammarVO);
		ContribucionD.getSons().add(EventoD);
		{
		String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
		
		CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		EventoD.getShows().add(VisibleAtt);
		EventoD.getShows().add(Valor2);
		EventoD.getShows().add(Valor3);
		}
		

		CompleteTextElementType FechaD=new CompleteTextElementType(NameConstantsMIGS.FECHA,ContribucionD,grammarVO);
		ContribucionD.getSons().add(FechaD);
		{
		String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
		
		CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		FechaD.getShows().add(VisibleAtt);
		FechaD.getShows().add(Valor2);
		FechaD.getShows().add(Valor3);
		}
		
		
		HashMap<Integer,HashMap<Integer, CompleteTextElementType>> tablaDat=new HashMap<Integer,HashMap<Integer, CompleteTextElementType>>();
		HashMap<Integer,List<CompleteTextElementType>> tablaDatList=new HashMap<Integer,List<CompleteTextElementType>>();
		
		try {
			ResultSet rs=MQL.RunQuerrySELECT("SELECT idov,contenido,num_ruta FROM metadatos WHERE ruta='/manifest/metadata/lom/lifecycle/contribute/centity/vcard' ORDER BY idov;");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String idov=rs.getObject("idov").toString();
					String valor=rs.getObject("num_ruta").toString();
					String contenido=rs.getObject("contenido").toString();
					
					
					if (idov!=null&&!idov.isEmpty()&&valor!=null&&!valor.isEmpty()&&contenido!=null&&!contenido.isEmpty())
						{
						try {
							Integer idovL = Integer.parseInt(idov);
							Integer DatoR=Integer.parseInt(Character.toString(valor.charAt(8)));
							
							List<CompleteTextElementType> TTT=tablaDatList.get(idovL);
							if (TTT==null)
								TTT=new ArrayList<CompleteTextElementType>();
							
							while (TTT.size()>=Contribucion.size())
								{
								CompleteTextElementType ContribucionD2=new CompleteTextElementType(NameConstantsMIGS.CONTRIBUCION,metadatos,grammarVO);
								metadatos.getSons().add(ContribucionD2);
								ContribucionD2.setClassOfIterator(ContribucionD);
								Contribucion.add(ContribucionD2);
								ContribucionD2.setMultivalued(true);
								{
								String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
								
								CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
								CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
								CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
								
								ContribucionD2.getShows().add(VisibleAtt);
								ContribucionD2.getShows().add(Valor2);
								ContribucionD2.getShows().add(Valor3);
								}
								
								CompleteTextElementType EventoD2=new CompleteTextElementType(NameConstantsMIGS.EVENTO,ContribucionD2,grammarVO);
								ContribucionD2.getSons().add(EventoD2);
								
								EventoD2.setClassOfIterator(EventoD);
								
								{
								String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
								
								CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
								CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
								CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
								
								EventoD2.getShows().add(VisibleAtt);
								EventoD2.getShows().add(Valor2);
								EventoD2.getShows().add(Valor3);
								}
								
								CompleteTextElementType FechaD2=new CompleteTextElementType(NameConstantsMIGS.FECHA,ContribucionD2,grammarVO);
								ContribucionD2.getSons().add(FechaD2);
								 FechaD2.setClassOfIterator( FechaD);
								
								{
								String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
								
								CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
								CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
								CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
								
								FechaD2.getShows().add(VisibleAtt);
								FechaD2.getShows().add(Valor2);
								FechaD2.getShows().add(Valor3);
								}
								
								
								}
							
							
							CompleteTextElementType este=Contribucion.get(TTT.size());
							
							TTT.add(este);
							tablaDatList.put(idovL, TTT);
							
							
							
							HashMap<Integer, CompleteTextElementType> Ht=tablaDat.get(idovL);
							if (Ht==null)
								Ht=new HashMap<Integer, CompleteTextElementType>();
							
							Ht.put(DatoR, este);
							
							tablaDat.put(idovL, Ht);
							
							CompleteDocuments CD=ObjetoVirtual.get(idovL);
							
							CompleteTextElement CTE=new CompleteTextElement(este, contenido);
							CD.getDescription().add(CTE);
							
							
							
						} catch (Exception e) {
							// TODO: handle exception
						}
						
						
						}
					else System.err.println("vacio en contribucion para idov" + idov);
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		try {
			ResultSet rs=MQL.RunQuerrySELECT("SELECT idov,contenido,num_ruta FROM metadatos WHERE ruta='/manifest/metadata/lom/lifecycle/contribute/role/value/langstring' ORDER BY idov;");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String idov=rs.getObject("idov").toString();
					String valor=rs.getObject("num_ruta").toString();
					String contenido=rs.getObject("contenido").toString();
					
					
					if (idov!=null&&!idov.isEmpty()&&valor!=null&&!valor.isEmpty()&&contenido!=null&&!contenido.isEmpty())
						{
						try {
							Integer idovL = Integer.parseInt(idov);
							Integer DatoR=Integer.parseInt(Character.toString(valor.charAt(8)));
							
						
							
							
							
							HashMap<Integer, CompleteTextElementType> Ht=tablaDat.get(idovL);
							if (Ht!=null)
								{
								CompleteTextElementType este = Ht.get(DatoR);
								if (este!=null)
									{
										for (CompleteElementType completeElementType : este.getSons()) {
											if (completeElementType instanceof CompleteTextElementType && completeElementType.getName().equals(NameConstantsMIGS.EVENTO))
												{
												CompleteDocuments CD=ObjetoVirtual.get(idovL);
												
												CompleteTextElement CTE=new CompleteTextElement((CompleteTextElementType) completeElementType, contenido);
												CD.getDescription().add(CTE);
												}
										}
									}
								}

							
						} catch (Exception e) {
							// TODO: handle exception
						}
						
						
						}
					else System.err.println("vacio en contribucion evento para idov" + idov);
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet rs=MQL.RunQuerrySELECT("SELECT idov,contenido,num_ruta FROM metadatos WHERE ruta='/manifest/metadata/lom/lifecycle/contribute/date/datetime' ORDER BY idov;");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String idov=rs.getObject("idov").toString();
					String valor=rs.getObject("num_ruta").toString();
					String contenido=rs.getObject("contenido").toString();
					
					
					if (idov!=null&&!idov.isEmpty()&&valor!=null&&!valor.isEmpty()&&contenido!=null&&!contenido.isEmpty())
						{
						try {
							Integer idovL = Integer.parseInt(idov);
							Integer DatoR=Integer.parseInt(Character.toString(valor.charAt(8)));
							
						
							
							
							
							HashMap<Integer, CompleteTextElementType> Ht=tablaDat.get(idovL);
							if (Ht!=null)
								{
								CompleteTextElementType este = Ht.get(DatoR);
								if (este!=null)
									{
										for (CompleteElementType completeElementType : este.getSons()) {
											if (completeElementType instanceof CompleteTextElementType && completeElementType.getName().equals(NameConstantsMIGS.FECHA))
												{
												CompleteDocuments CD=ObjetoVirtual.get(idovL);
												
												CompleteTextElement CTE=new CompleteTextElement((CompleteTextElementType) completeElementType, contenido);
												CD.getDescription().add(CTE);
												}
										}
									}
								}

							
						} catch (Exception e) {
							// TODO: handle exception
						}
						
						
						}
					else System.err.println("vacio en contribucion fecha para idov" + idov);
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



	private void processTaxonValues(HashMap<String, CompleteTextElementType> tablaCatalogos) {
HashMap<Integer,HashMap<Integer, CompleteTextElementType>> tablaDat=new HashMap<Integer,HashMap<Integer, CompleteTextElementType>>();
		
		try {
			ResultSet rs=MQL.RunQuerrySELECT("SELECT DISTINCT idov,num_ruta,contenido FROM metadatos WHERE ruta='/manifest/metadata/lom/classification/taxonpath/source/langstring' ORDER BY idov;");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String idov=rs.getObject("idov").toString();
					String valor=rs.getObject("num_ruta").toString();
					String contenido=rs.getObject("contenido").toString();
					
					
					if (idov!=null&&!idov.isEmpty()&&valor!=null&&!valor.isEmpty()&&contenido!=null&&!contenido.isEmpty())
						{
						try {
							Integer idovL = Integer.parseInt(idov);
							Integer DatoR=Integer.parseInt(Character.toString(valor.charAt(8)));
							
							HashMap<Integer, CompleteTextElementType> Ht=tablaDat.get(idovL);
							if (Ht==null)
								Ht=new HashMap<Integer, CompleteTextElementType>();
							
							CompleteTextElementType contenidoDat=tablaCatalogos.get(contenido);
							Ht.put(DatoR, contenidoDat);
							
							tablaDat.put(idovL, Ht);
							
							
							
						} catch (Exception e) {
							// TODO: handle exception
						}
						
						
						}
					else System.err.println("vacio en taxonomia para idov" + idov);
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		try {
			ResultSet rs=MQL.RunQuerrySELECT("SELECT DISTINCT idov,num_ruta,contenido FROM metadatos WHERE ruta='/manifest/metadata/lom/classification/taxonpath/taxon/entry/langstring' ORDER BY idov;");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String idov=rs.getObject("idov").toString();
					String valor=rs.getObject("num_ruta").toString();
					String contenido=rs.getObject("contenido").toString();
					
					
					if (idov!=null&&!idov.isEmpty()&&valor!=null&&!valor.isEmpty()&&contenido!=null&&!contenido.isEmpty())
						{
						try {
							Integer idovL = Integer.parseInt(idov);
							Integer DatoR=Integer.parseInt(Character.toString(valor.charAt(8)));
							
							HashMap<Integer, CompleteTextElementType> Ht=tablaDat.get(idovL);
							if (Ht!=null)
								{
								CompleteTextElementType futuro=Ht.get(DatoR);
								if (futuro!=null)
									{
									CompleteDocuments CD=ObjetoVirtual.get(idovL);
									
									CompleteTextElement CTE=new CompleteTextElement(futuro, contenido);
									CD.getDescription().add(CTE);
									}
								}
							
						
							
						} catch (Exception e) {
							// TODO: handle exception
						}
						
						
						}
					else System.err.println("vacio valor en taxonomia para idov" + idov);
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}



	private void processCatalogsValues(HashMap<String, CompleteTextElementType> tablaCatalogos) {
		
		
		HashMap<Integer,HashMap<Integer, CompleteTextElementType>> tablaDat=new HashMap<Integer,HashMap<Integer, CompleteTextElementType>>();
		
		try {
			ResultSet rs=MQL.RunQuerrySELECT("SELECT DISTINCT idov,num_ruta,contenido FROM metadatos WHERE ruta='/manifest/metadata/lom/general/catalogentry/catalog' ORDER BY idov;");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String idov=rs.getObject("idov").toString();
					String valor=rs.getObject("num_ruta").toString();
					String contenido=rs.getObject("contenido").toString();
					
					
					if (idov!=null&&!idov.isEmpty()&&valor!=null&&!valor.isEmpty()&&contenido!=null&&!contenido.isEmpty())
						{
						try {
							Integer idovL = Integer.parseInt(idov);
							Integer DatoR=Integer.parseInt(Character.toString(valor.charAt(8)));
							
							HashMap<Integer, CompleteTextElementType> Ht=tablaDat.get(idovL);
							if (Ht==null)
								Ht=new HashMap<Integer, CompleteTextElementType>();
							
							CompleteTextElementType contenidoDat=tablaCatalogos.get(contenido);
							Ht.put(DatoR, contenidoDat);
							
							tablaDat.put(idovL, Ht);
							
							
							
						} catch (Exception e) {
							// TODO: handle exception
						}
						
						
						}
					else System.err.println("ErrorIdovNotFound");
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		try {
			ResultSet rs=MQL.RunQuerrySELECT("SELECT DISTINCT idov,num_ruta,contenido FROM metadatos WHERE ruta='/manifest/metadata/lom/general/catalogentry/entry/langstring' ORDER BY idov;");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String idov=rs.getObject("idov").toString();
					String valor=rs.getObject("num_ruta").toString();
					String contenido=rs.getObject("contenido").toString();
					
					
					if (idov!=null&&!idov.isEmpty()&&valor!=null&&!valor.isEmpty()&&contenido!=null&&!contenido.isEmpty())
						{
						try {
							Integer idovL = Integer.parseInt(idov);
							Integer DatoR=Integer.parseInt(Character.toString(valor.charAt(8)));
							
							HashMap<Integer, CompleteTextElementType> Ht=tablaDat.get(idovL);
							if (Ht!=null)
								{
								CompleteTextElementType futuro=Ht.get(DatoR);
								if (futuro!=null)
									{
									CompleteDocuments CD=ObjetoVirtual.get(idovL);
									
									CompleteTextElement CTE=new CompleteTextElement(futuro, contenido);
									CD.getDescription().add(CTE);
									}
								}
							
						
							
						} catch (Exception e) {
							// TODO: handle exception
						}
						
						
						}
					else System.err.println("ErrorIdovNotFound");
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}



	private void procesaDatos(CompleteElementType datos,CompleteGrammar CG) {
		
		ArrayList<String> Categorias=new ArrayList<String>();
		
		try {
			ResultSet rs=MQL.RunQuerrySELECT("SELECT DISTINCT categoria FROM atributos_numericos;");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String categoria=rs.getObject("categoria").toString();

					if (categoria!=null&&!categoria.isEmpty())
						{
						Categorias.add(categoria);
						}
					else System.err.println("ErrorIdovNotFound");
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		for (String categoria : Categorias) {
			procesaCategoriaNUM(categoria,datos,CG);
		}
		
		Categorias=new ArrayList<String>();
		
		try {
			ResultSet rs=MQL.RunQuerrySELECT("SELECT DISTINCT categoria FROM atributos_texto;");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String categoria=rs.getObject("categoria").toString();

					if (categoria!=null&&!categoria.isEmpty())
						{
						Categorias.add(categoria);
						}
					else System.err.println("ErrorIdovNotFound");
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		for (String categoria : Categorias) {
			procesaCategoriaTEXT(categoria,datos,CG);
		}
	}



	private void procesaCategoriaTEXT(String categoria, CompleteElementType padre, CompleteGrammar CG) {
		CompleteElementType Datos=new CompleteElementType(categoria,padre,CG);
		padre.getSons().add(Datos);
		{
		String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
		
		CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		Datos.getShows().add(VisibleAtt);
		Datos.getShows().add(Valor2);
		Datos.getShows().add(Valor3);
		
		}
		
		ArrayList<String> atributosCat=new ArrayList<String>();
		
		
		try {
			ResultSet rs=MQL.RunQuerrySELECT("SELECT DISTINCT nom_atrib FROM atributos_texto WHERE categoria='"+categoria+"';");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String nom_atrib=rs.getObject("nom_atrib").toString();

					if (nom_atrib!=null&&!nom_atrib.isEmpty())
						{
						atributosCat.add(nom_atrib);
						}
					else System.err.println("ErrorIdovNotFound");
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		for (String atribcate : atributosCat) {
			procesaCategoriaAttreTEX(categoria,atribcate,Datos,CG);
		}
		
	}



	private void procesaCategoriaAttreTEX(String categoria, String atribcate, CompleteElementType padre,
			CompleteGrammar CG) {
		CompleteTextElementType atributo=new CompleteTextElementType(atribcate,padre,CG);
		padre.getSons().add(atributo);
		{
		String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
		
		CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		atributo.getShows().add(VisibleAtt);
		atributo.getShows().add(Valor2);
		atributo.getShows().add(Valor3);
		
		}
		

		
		
		
		try {
			ResultSet rs=MQL.RunQuerrySELECT("SELECT DISTINCT idov,valor FROM atributos_texto WHERE categoria='"+categoria+"' AND nom_atrib='"+atribcate+"';");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String idov=rs.getObject("idov").toString();
					String valor=rs.getObject("valor").toString();

					if (idov!=null&&!idov.isEmpty()&&valor!=null&&!valor.isEmpty())
						{
						Integer idovL = Integer.parseInt(idov);
							try {
								CompleteDocuments CD=ObjetoVirtual.get(idovL);
								
								CompleteTextElement CTE=new CompleteTextElement(atributo, valor);
								CD.getDescription().add(CTE);
								
								
								
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					else System.err.println("ErrorIdovNotFound");
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}



	private void procesaCategoriaNUM(String categoria, CompleteElementType padre,CompleteGrammar CG) {
		CompleteElementType Datos=new CompleteElementType(categoria,padre,CG);
		padre.getSons().add(Datos);
		{
		String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
		
		CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		Datos.getShows().add(VisibleAtt);
		Datos.getShows().add(Valor2);
		Datos.getShows().add(Valor3);
		
		}
		
		ArrayList<String> atributosCat=new ArrayList<String>();
		
		
		try {
			ResultSet rs=MQL.RunQuerrySELECT("SELECT DISTINCT nom_atrib FROM atributos_numericos WHERE categoria='"+categoria+"';");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String nom_atrib=rs.getObject("nom_atrib").toString();

					if (nom_atrib!=null&&!nom_atrib.isEmpty())
						{
						atributosCat.add(nom_atrib);
						}
					else System.err.println("ErrorIdovNotFound");
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		for (String atribcate : atributosCat) {
			procesaCategoriaAttNU(categoria,atribcate,Datos,CG);
		}
		
	}



	private void procesaCategoriaAttNU(String categoria, String atribcate, CompleteElementType padre, CompleteGrammar CG) {
		CompleteTextElementType atributo=new CompleteTextElementType(atribcate,padre,CG);
		padre.getSons().add(atributo);
		{
		String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
		
		CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		atributo.getShows().add(VisibleAtt);
		atributo.getShows().add(Valor2);
		atributo.getShows().add(Valor3);
		
		}
		
		CompleteTextElementType unidadAtr=new CompleteTextElementType(NameConstantsMIGS.UNIDADES,padre,CG);
		atributo.getSons().add(unidadAtr);
		{
		String VistaOV=new String(NameConstantsMIGS.PRESNTACION);
		
		CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsMIGS.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsMIGS.BROWSERSHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsMIGS.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		unidadAtr.getShows().add(VisibleAtt);
		unidadAtr.getShows().add(Valor2);
		unidadAtr.getShows().add(Valor3);
		
		}
		
		
		
		try {
			ResultSet rs=MQL.RunQuerrySELECT("SELECT DISTINCT idov,valor,unidades FROM atributos_numericos WHERE categoria='"+categoria+"' AND nom_atrib='"+atribcate+"';");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String idov=rs.getObject("idov").toString();
					String valor=rs.getObject("valor").toString();
					String unidades=rs.getObject("unidades").toString();

					if (idov!=null&&!idov.isEmpty()&&valor!=null&&!valor.isEmpty())
						{
						Integer idovL = Integer.parseInt(idov);
							try {
								CompleteDocuments CD=ObjetoVirtual.get(idovL);
								
								CompleteTextElement CTE=new CompleteTextElement(atributo, valor);
								CD.getDescription().add(CTE);
								
								if (unidades!=null&&!unidades.isEmpty())
								{
									CompleteTextElement CTEU=new CompleteTextElement(unidadAtr, unidades);
									CD.getDescription().add(CTEU);
								}
								
								
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					else System.err.println("ErrorIdovNotFound");
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
	
	/**
	 * QUitar caracteres especiales.
	 * @param str texto de entrada.
	 * @return texto sin caracteres especiales.
	 */
	public String RemoveSpecialCharacters(String str) {
		   StringBuilder sb = new StringBuilder();
		   for (int i = 0; i < str.length(); i++) {
			   char c = str.charAt(i);
			   if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_') {
			         sb.append(c);
			      }
		}
		   return sb.toString();
		}

}
