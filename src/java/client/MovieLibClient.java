
import ser321.movie.MovieLibraryGui;

import java.net.Socket;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.awt.event.*;

/**
 * Copyright [2016] [Eric Kaufman]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 * Created by Eric on 8/29/2016.
 */

public class MovieLibClient extends MovieLibraryGui implements
        TreeWillExpandListener,
        ActionListener,
        TreeSelectionListener {

    private static final boolean debugOn = true;
    private static final boolean bootstrapOn = false;
    private boolean stopPlaying;         //shared with playing thread.
    private MovieLibClientProxy clientProxy;


    public MovieLibClient(String author, MovieLibClientProxy clientProxy) {
        super(author);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.clientProxy = clientProxy;
        stopPlaying = false;
        plotJTA.setText("You selected: ");

        actorsJCB.addActionListener(this);
        actorsJCB.setActionCommand("NewActor");
        actorsClearButton.addActionListener(this);
        actorsClearButton.setActionCommand("ClearActors");

        genreJCB.addActionListener(this);
        genreJCB.setActionCommand("NewGenre");
        genreClearButton.addActionListener(this);
        genreClearButton.setActionCommand("ClearGenre");
        genreJCB.setEditable(true);
        actorsJCB.setEditable(true);

        for(int i=0; i<userMenuItems.length; i++){
            for(int j=0; j<userMenuItems[i].length; j++){
                userMenuItems[i][j].addActionListener(this);
            }
        }
        //tree.addTreeWillExpandListener(this);
        try{
            tree.addTreeSelectionListener(this);
            rebuildTree();
        }catch (Exception ex){
            JOptionPane.showMessageDialog(this,"Handling "+
                    " constructor exception: " + ex.getMessage());
        }
        setVisible(true);
    }

    private void debug(String message) {
        if (debugOn)
            System.out.println("debug: "+message);
    }

    /**
     * create and initialize nodes in the JTree of the left pane.
     * buildInitialTree is called by MovieLibraryGui to initialize the JTree.
     * Classes that extend MovieLibraryGui should override this method to
     * perform initialization actions specific to the extended class.
     * The default functionality is to set base as the label of root.
     * In your solution, you will probably want to initialize by deserializing
     * your library and displaying the categories and subcategories in the
     * tree.
     * @param root Is the root node of the tree to be initialized.
     * @param base Is the string that is the root node of the tree.
     */
    public void buildInitialTree(DefaultMutableTreeNode root, String base){
        //set up the context and base name
        try{
            root.setUserObject(base);
        }catch (Exception ex){
            JOptionPane.showMessageDialog(this,"exception initial tree:"+ex);
            ex.printStackTrace();
        }
    }

    public void rebuildTree(){
        String[] videoList = clientProxy.getTitles();
        String[] videoGenre = {"Action","Action","Biography","Comedy","Comedy","Comedy"};
        tree.removeTreeSelectionListener(this);
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
        clearTree(root, model);
        DefaultMutableTreeNode videoNode = new DefaultMutableTreeNode("Video");
        model.insertNodeInto(videoNode, root, model.getChildCount(root));
        // put nodes in the tree for all video
        for (int i = 0; i<videoList.length; i++){
            String aTitle = videoList[i];
            //String aGenre = videoGenre[i];
            DefaultMutableTreeNode toAdd = new DefaultMutableTreeNode(aTitle);
            videoNode.add(toAdd);
        }
        // expand all the nodes in the JTree
        for(int r =0; r < tree.getRowCount(); r++){
            tree.expandRow(r);
        }
        tree.addTreeSelectionListener(this);
    }

    private void clearTree(DefaultMutableTreeNode root, DefaultTreeModel model){
        try{
            DefaultMutableTreeNode next = null;
            int subs = model.getChildCount(root);
            for(int k=subs-1; k>=0; k--){
                next = (DefaultMutableTreeNode)model.getChild(root,k);
                debug("removing node labelled:"+(String)next.getUserObject());
                model.removeNodeFromParent(next);
            }
        }catch (Exception ex) {
            System.out.println("Exception while trying to clear tree:");
            ex.printStackTrace();
        }
    }

    private DefaultMutableTreeNode getSubLabelled(DefaultMutableTreeNode root,
                                                  String label){
        DefaultMutableTreeNode ret = null;
        DefaultMutableTreeNode next = null;
        boolean found = false;
        for(Enumeration e = root.children(); e.hasMoreElements();){
            next = (DefaultMutableTreeNode)e.nextElement();
            debug("sub with label: "+(String)next.getUserObject());
            if (((String)next.getUserObject()).equals(label)){
                debug("found sub with label: "+label);
                found = true;
                break;
            }
        }
        if(found)
            ret = next;
        else
            ret = null;
        return ret;
    }

    public void treeWillCollapse(TreeExpansionEvent tee) {
        debug("In treeWillCollapse with path: "+tee.getPath());
        tree.setSelectionPath(tee.getPath());
    }

    public void treeWillExpand(TreeExpansionEvent tee) {
        debug("In treeWillExpand with path: "+tee.getPath());
        //DefaultMutableTreeNode dmtn =
        //    (DefaultMutableTreeNode)tee.getPath().getLastPathComponent();
        //System.out.println("will expand node: "+dmtn.getUserObject()+
        //		   " whose path is: "+tee.getPath());
    }

    public void valueChanged(TreeSelectionEvent e) {
        try{
            tree.removeTreeSelectionListener(this);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    tree.getLastSelectedPathComponent();
            if(node!=null){
                String nodeLabel = (String)node.getUserObject();
                debug("In valueChanged. Selected node labelled: "+nodeLabel);
                // is this a terminal node?
                if(node.getChildCount()==0 &&
                        (node != (DefaultMutableTreeNode)tree.getModel().getRoot())){
                    MovieDescription movieDes = clientProxy.get(nodeLabel);
                    plotJTA.setText(movieDes.getPlot());
                    ratedJTF.setText(movieDes.getRated());
                    fileNameJTF.setText(movieDes.getFilename());
                    runtimeJTF.setText(movieDes.getRuntime());
                    releasedJTF.setText(movieDes.getReleased());
                    actorsJCB.removeAllItems();
                    for(String s: movieDes.getActors()){
                        actorsJCB.addItem(s);
                    }
                    actorsJCB.setSelectedIndex(0);
                    genreJCB.removeAllItems();
                    for(String s: movieDes.getGenre()){
                        genreJCB.addItem(s);
                    }
                    titleJTF.setText(nodeLabel);
                    if (!contains(genreJCB, nodeLabel)){
                        genreJCB.removeActionListener(this);
                        //genreJCB.addItem(nodeLabel);
                        int i = 0;
                        while(i<genreJCB.getItemCount()&&
                                !((String)genreJCB.getItemAt(i)).equals(nodeLabel)){
                            i++;
                        }
                        //genreJCB.setSelectedIndex(i);
                        genreJCB.addActionListener(this);
                    }
                }else{
                    plotJTA.setText("You selected: ");
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        tree.addTreeSelectionListener(this);
    }

    // a method to determine whether a string is already in the combo box
    private boolean contains(JComboBox jcb, String text){
        boolean ret = false;
        for (int i=0; i< jcb.getItemCount(); i++){
            if (((String)jcb.getItemAt(i)).equals(text)){
                ret = true;
            }
        }
        return ret;
    }

    public void actionPerformed(ActionEvent e) {
        tree.removeTreeSelectionListener(this);
        if(e.getActionCommand().equals("Exit")) {
            System.exit(0);
        }
         else if(e.getActionCommand().equals("ClearActors")) {
            actorsJCB.removeActionListener(this);
            actorsJCB.removeAllItems();
            actorsJCB.addActionListener(this);
        }else if(e.getActionCommand().equals("NewActor")) {
            //debug("new actor selected "+(String)actorsJCB.getSelectedItem());
            if(actorsJCB.getSelectedItem() != null) {
                if (!contains(actorsJCB, (String) actorsJCB.getSelectedItem())) {
                    actorsJCB.addItem((String) actorsJCB.getSelectedItem());
                }
            }}
        else if(e.getActionCommand().equals("NewGenre")) {
            //debug("new actor selected "+(String)actorsJCB.getSelectedItem());
            if(genreJCB.getSelectedItem() != null){
                if(!contains(genreJCB,(String)genreJCB.getSelectedItem())){
                    genreJCB.addItem((String)genreJCB.getSelectedItem());
                }}
        }
        else if(e.getActionCommand().equals("ClearGenre")) {
            genreJCB.removeActionListener(this);
            genreJCB.removeAllItems();
            genreJCB.addActionListener(this);
        }else if(e.getActionCommand().equals("Save")) {
            plotJTA.append("Save, ");
            rebuildTree();
            // what to do with boolean returns by server methods
            debug("Save "+((true)?"successful":"unsuccessful"));
        }else if(e.getActionCommand().equals("Restore")) {
            plotJTA.append("Restore, ");
        }else if(e.getActionCommand().equals("Tree Refresh")) {
           // plotJTA.append("Tree Refresh, ");
            rebuildTree(); // contact the server to obtain all current titles then rebuild
        }else if(e.getActionCommand().equals("Add")) {
            Vector<String> actors = new Vector<>();
            Vector<String> genre = new Vector<>();
            for(int i = 0; i < actorsJCB.getItemCount(); i++){
                actors.add(actorsJCB.getItemAt(i));
            }
            for(int i = 0; i < genreJCB.getItemCount(); i++){
                genre.add(genreJCB.getItemAt(i));
            }

            MovieDescription movieDes = new MovieDescription(titleJTF.getText(), ratedJTF.getText(), releasedJTF.getText(),
                    runtimeJTF.getText(), plotJTA.getText(), fileNameJTF.getText(), genre, actors);
            clientProxy.add(movieDes);
            rebuildTree();
        }else if(e.getActionCommand().equals("Remove")) {
            clientProxy.remove(titleJTF.getText());
            rebuildTree();
           // plotJTA.append("Remove, ");
        }else if(e.getActionCommand().equals("Play")){
            plotJTA.append("Play, ");
            try{
                String nodeLabel = "Machu Picchu Time Lapse";
                titleJTF.setText(nodeLabel);
                String aURIPath = "file://"+System.getProperty("user.dir")+
                        "/MachuPicchuTimelapseVimeo.mp4";
                playMovie(aURIPath, nodeLabel);
            }catch(Exception ex){
                System.out.println("Execption trying to play movie:");
                ex.printStackTrace();
            }
        }
        tree.addTreeSelectionListener(this);
    }

    public boolean sezToStop(){
        return stopPlaying;
    }

    public static void main(String args[]) {

        if (args.length != 2) {
            System.out.println("Usage: java ser321.sockets.MovieLibClient hostName "+
                    "portNumber");
            System.exit(0);
        }
        String host = args[0];
        int portNo = Integer.parseInt(args[1]);
        try {
            MovieLibClientProxy clientProxy = new MovieLibClientProxy(host, portNo, "1");
            String authorName = "Dr Lindquists Library";
            if(args.length >=1){
                authorName = args[0];
            }
            System.out.println("calling constructor name "+authorName);
            MovieLibClient mla = new MovieLibClient(authorName, clientProxy);
        }catch (Exception ex){
            System.out.println("Exception in main: "+ex.getMessage());
            ex.printStackTrace();
        }
    }
}
