/**
 * 
 */
package fdi.ucm.server.migs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import fdi.ucm.server.modelComplete.ImportExportDataEnum;
import fdi.ucm.server.modelComplete.ImportExportPair;
import fdi.ucm.server.modelComplete.LoadCollection;
import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.CompleteCollectionAndLog;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteElementType;
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
		GrammarVO.getSons().add(Datos);
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
	}



	private void procesaMetaDatos(CompleteElementType metadatos, CompleteGrammar grammarVO) {
ArrayList<String> Categorias=new ArrayList<String>();
		
		try {
			ResultSet rs=MQL.RunQuerrySELECT("SELECT DISTINCT contenido FROM metadatos WHERE ruta='/manifest/metadata/lom/general/catalogentry/catalog';");
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
		
		
		
		
ArrayList<String> Categorias=new ArrayList<String>();
		
		try {
			ResultSet rs=MQL.RunQuerrySELECT("SELECT DISTINCT contenido FROM metadatos WHERE ruta='/manifest/metadata/lom/classification/taxonpath/source/langstring';");
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
