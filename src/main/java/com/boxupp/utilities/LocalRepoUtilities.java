package com.boxupp.utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import com.boxupp.db.beans.LocalGitRepoBean;
import com.boxupp.responseBeans.StatusBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LocalRepoUtilities {
	private static Logger logger = LogManager.getLogger(LocalRepoUtilities.class.getName());
	private static LocalRepoUtilities localRepoUtilities = null;
	
	public static LocalRepoUtilities getInstance() {
		if (localRepoUtilities == null) {
			try {
				localRepoUtilities = new LocalRepoUtilities();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return localRepoUtilities;
	}

	public List<String> fetchBranches(HttpServletRequest request){
		
		String localRepoPath = request.getParameter("localRepoPath");
		   List<String> branchList = new ArrayList<String>();
		   List<Ref> call;
		   Repository repository = null;
		   try {
			   /*FileRepositoryBuilder builder = new FileRepositoryBuilder();
		      repository = builder.readEnvironment() // scan environment GIT_* variables
					        .findGitDir() // scan up the file system tree
					        .build();
*/		      call = Git.open( new File(localRepoPath + "/.git" ) ).branchList().setListMode(ListMode.REMOTE).call();
		      
		      
	          for (Ref ref : call) {
				branchList.add(ref.getName().split("\\/")[ ref.getName().split("\\/").length-1]);
			  }
			}catch (Exception e) {
				logger.error("Error in fetching repo branch list "+e);
			}
	       	return branchList;
	   	}

	

	public StatusBean commitOnRemoteRepo(JsonNode commitData) {
		StatusBean stBean = new StatusBean();
		Gson projectData = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss").create();
		LocalGitRepoBean localRepoBean = projectData.fromJson(commitData.toString(), LocalGitRepoBean.class);
	    String username = localRepoBean.getGitURI().split("@")[0]; 
	    try {   
	    // credentials
	        CredentialsProvider cp = new UsernamePasswordCredentialsProvider(username, localRepoBean.getPassword());
	      
			 Git git =  Git.open( new File( localRepoBean.getLocalRepoPath() + "/.git" ) );
			//add	
	        AddCommand ac = git.add();
	        ac.addFilepattern(localRepoBean.getPath());
	        ac.call();
	       
	        // commit
	        CommitCommand commit = git.commit();
	        commit.setMessage(localRepoBean.getComment());
	        commit.call();
	        
	        // push
	        PushCommand pc = git.push();
	        pc.setCredentialsProvider(cp).setRemote("origin").setForce(true).setPushAll();
	        Iterator<PushResult> it = pc.call().iterator();
	          if(it.hasNext()){
	               System.out.println(it.next().toString());
	          }
	        stBean.setStatusCode(0);
	        stBean.setStatusMessage("Commited Successfully");
	    }catch(Exception e){
	    	logger.error("Error in commiting code on remote repo :"+e);
	    	stBean.setStatusCode(1);
	    	stBean.setStatusMessage("Error in commiting code on remote repo ");
	    }
		return stBean;

	}
	public static void main(String[] args) {
		/*String name = "paxgit";
		String password = "paxcel!@2345";
		String url = "paxgit@172.16.0.46:/home/paxgit/boxupp";*/
		String name = "ashachhikara";
		String password = "Jhajjar1979";
		String url = "https://github.com/ashachhikara/box.git";
        // credentials
        CredentialsProvider cp = new UsernamePasswordCredentialsProvider(name, password);
        // clone
        //File dir = new File("/tmp/abf");
        /*CloneCommand cc = new CloneCommand()
                .setCredentialsProvider(cp)
                .setDirectory(dir)
                .setURI(url);*/
        Git git = null;
		try {
			git = Git.open( new File( "/tmp/abf" + "/.git" ) );
			//git = cc.call();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
        // add
        AddCommand ac = git.add();
        ac.addFilepattern("Vagrantfile");
        try {
            ac.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        // commit
        CommitCommand commit = git.commit()
       .setMessage("push war");
        try {
            commit.call();
        } catch (Exception e) {
            e.printStackTrace();
        } // push
        PushCommand pc = git.push();
        pc.setCredentialsProvider(cp)
                .setForce(true)
                .setPushAll();
        try {
            Iterator<PushResult> it = pc.call().iterator();
            if(it.hasNext()){
                System.out.println(it.next().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
 
       
	}

	
}
