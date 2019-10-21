package graph.main;



import cim.Model;
import graph.graphFactory.GraphEPSFactory;
import graph.graphFactory.ReducedGraphEPSFactory;
import graph.graphs.Graph;
import graph.graphs.ReducedGraphEPS;
import graph.searcher.LoopSearcher;
import graph.searcher.ParallelSearcher;
import graph.searcher.ZoneWithoutEnergySearcher;
import graph.structs.Edge;
import graph.structs.Node;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	
    	//Model.getInstance().loadFromXML("C:\\Users\\PC\\Desktop\\quepasa\\model.cim");
    	//Model.getInstance().loadFromXML("C:\\Users\\PC\\Desktop\\conOtrosCaminos\\model.cim");
    	//Model.getInstance().loadFromXML("C:\\Users\\PC\\Desktop\\conCaminos\\model.cim");
    	//Model.getInstance().loadFromXML("C:\\Users\\PC\\Desktop\\Armstrong\\model.cim");
    	Model.getInstance().loadFromXML("C:\\Users\\PC\\Desktop\\ejemploparaterminar\\model.cim");
    	long startTimeCreateGraph = System.nanoTime();
    	GraphEPSFactory graphEPSCreator= new GraphEPSFactory(Model.getInstance());
    	Graph<Node,Edge> graphEPS= graphEPSCreator.createGraph();
    	long endTimeCreateGraph = System.nanoTime() - startTimeCreateGraph;
    	graphEPS.printGraph();
    	
    	long startTimeCreateReducedGraph = System.nanoTime();
    	ReducedGraphEPSFactory reduceGraphEPSCreator= new ReducedGraphEPSFactory(graphEPS);
    	ReducedGraphEPS reduceGraphEPS= reduceGraphEPSCreator.createGraph();
    	long endTimeCreateReducedGraph = System.nanoTime() - startTimeCreateReducedGraph;
    	reduceGraphEPS.printGraph();
    	
    
    	/*
    	long startTimeZoneWithoutEnergy = System.nanoTime();
    	ZoneWithoutEnergySearcher withoutEnergySearcher= new ZoneWithoutEnergySearcher(reduceGraphEPS);
    	withoutEnergySearcher.search();
    	long endTimeZoneWithoutEnergy = System.nanoTime() - startTimeZoneWithoutEnergy;
    	long startTimeTranslateZoneWithoutEnergy = System.nanoTime();
    	withoutEnergySearcher.print();
    	long endTimeTranslateZoneWithoutEnergy = System.nanoTime() - startTimeTranslateZoneWithoutEnergy;*/
    	
    	/*
    	long startTimeLoopSearcher = System.nanoTime();
    	LoopSearcher loopSearcher = new LoopSearcher(reduceGraphEPS);
    	loopSearcher.search();
    	long endTimeLoopSearcher = System.nanoTime() - startTimeLoopSearcher;
    	long startTimeTranslateLoopSearcher = System.nanoTime();
    	//loopSearcher.printLoops();
    	loopSearcher.printTranlatedLoops();
    	long endTimeTranslateLoopSearcher = System.nanoTime() - startTimeTranslateLoopSearcher;*/
    	
    	/*
    	long startTimeParallelSearcher = System.nanoTime();
    	ParallelSearcher parallelSearcher=new ParallelSearcher(reduceGraphEPS);
    	parallelSearcher.search();
    	long endTimeParallelSearcher = System.nanoTime() - startTimeParallelSearcher;
    	//parallelSearcher.printParallels();
    	long startTimeTranslateParallelSearcher = System.nanoTime();
    	parallelSearcher.printTranslatedParallels();
    	long endTimeTranslateParallelSearcher = System.nanoTime() - startTimeTranslateParallelSearcher;*/
    	
    	//System.out.println("tiempo en crear el grafo: " + endTimeCreateGraph/ 1e6 + " ms");
    	//System.out.println("tiempo en crear el grafo reducido: " + endTimeCreateReducedGraph/ 1e6 + " ms");
    	//System.out.println("tiempo en calcular zonas desenergizadas: " + endTimeZoneWithoutEnergy/ 1e6 + " ms");
    	//System.out.println("tiempo en calcular traducir zonas desenergizadas: " + endTimeTranslateZoneWithoutEnergy/ 1e6 + " ms");
    	//System.out.println("tiempo en calcular lazos: " + endTimeLoopSearcher/ 1e6 + " ms");
    	//System.out.println("tiempo en traducir lazos: " + endTimeTranslateLoopSearcher/ 1e6 + " ms");
    	//System.out.println("tiempo en calcular paralelos: " + endTimeParallelSearcher/ 1e6 + " ms");
    	//System.out.println("tiempo en traducir paralelos: " + endTimeTranslateParallelSearcher/ 1e6 + " ms");
    }
}
