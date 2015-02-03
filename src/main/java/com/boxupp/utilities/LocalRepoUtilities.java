package com.boxupp.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonNode;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import com.boxupp.responseBeans.StatusBean;

public class LocalRepoUtilities {

	private static LocalRepoUtilities localRepoUtilities = null;
	  private String localPath, remotePath;
	    private Repository remoteRepo;
	    private Git git;

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

	  public void init() throws IOException {
	        localPath = "/tmp/repo/myrepo";
	        remotePath = "ashachhikara@github.com/ashachhikara/box.git";
	        remoteRepo = new FileRepository(remotePath);
	        git = new Git(remoteRepo);
	       // LocalRepoUtilities.getInstance().testCreate();
	      
	        try {
	        	//LocalRepoUtilities.getInstance().testClone();
				LocalRepoUtilities.getInstance().testAdd();
				LocalRepoUtilities.getInstance().testCommit();
				LocalRepoUtilities.getInstance().testPush();
			} catch (GitAPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

	   
	    public void testCreate() throws IOException {
	        Repository newRepo = new FileRepository(localPath + "/.git");
	        newRepo.create();
	        System.out.println(newRepo);
	       
	    }

	   public List<String> getBranches(String remotePath){
		   List<String> branchList = new ArrayList<String>();
		   List<Ref> call;
		   try {
				call = git.branchList().setListMode(ListMode.REMOTE).call();
	
				for (Ref ref : call) {
					int listLength = ref.getObjectId().getName().split(" ")[0].split("/").length;
					branchList.add(ref.getObjectId().getName().split(" ")[0].split("/")[listLength-1]);
					System.out.println("Branch: " + ref + " " + ref.getObjectId().getName() + " " + ref.getObjectId().getName());
				}
			}catch (GitAPIException e) {
			
			}
	       	return branchList;
	   	}
	   
	    public void testClone() throws IOException, GitAPIException {
	    	Git.cloneRepository().setURI(remotePath)
            .setDirectory(new File(localPath)).call();
	    	/*final SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
	    		
	    		@Override
				protected void configure(Host hc, Session session) {
					   session.setPassword( "paxcel!@2345" );
					
				}
	    	};
	    		
	    		CloneCommand cloneCommand = Git.cloneRepository();
	    		
	    		cloneCommand.setURI( "paxgit@172.16.0.46:/home/paxgit/Boxupp.git");
	    		
	    		cloneCommand.setTransportConfigCallback( new TransportConfigCallback() {
	    		
	    		  @Override
	    		
	    		  public void configure( Transport transport ) {
	    		
	    		    SshTransport sshTransport = ( SshTransport )transport;
	    		
	    		    sshTransport.setSshSessionFactory( sshSessionFactory );
	    		
	    		  }
	    		
	    		} );*/
	    		//cloneCommand.setDirectory(new File(localPath)).call();
	    }

	    public void testAdd() throws IOException, GitAPIException {
	       // File myfile = new File(localPath + "/Vagrantfile");
	       // myfile.createNewFile();
	       // git.add().addFilepattern("Vagrantfile").call();
	    	  File myfile = new File(localPath + "/Vagrantfile");
	          myfile.createNewFile();
	          git.add().addFilepattern("Vagrantfile").call();
	        /*CredentialsProvider cp = new UsernamePasswordCredentialsProvider("paxgit", "paxcel!@2345");
	        Collection<Ref> remoteRefs = git.lsRemote()
	            .setCredentialsProvider(cp) .setRemote("origin")     
	            .setTags(true)
	            .setHeads(false)
	            .call();
	        for (Ref ref : remoteRefs) {
	            System.out.println(ref.getName() + " -> " + ref.getObjectId().name());
	        }
	             */ 
	    }


	    public StatusBean testCommit() {
	    	StatusBean stBean = new StatusBean();
	    	stBean.setStatusCode(1);
	        try {
				git.commit().setMessage("Added myfile").call();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        return stBean;
	        		
	    }

	
	    public void testPush() throws IOException, JGitInternalException,
	            GitAPIException {
	        git.push().setPushAll().setRemote("origin").call();
	    }

	    public void testTrackMaster() throws IOException, JGitInternalException,
	            GitAPIException {
	        git.branchCreate().setName("master")
	                .setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
	                .setStartPoint("origin/master").setForce(true).call();
	    }

	    public void testPull() throws IOException, GitAPIException {
	        git.pull().call();
	    }
	
	public static void main(String[] args){
		try {
			LocalRepoUtilities.getInstance().init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> getLocalRepos(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public StatusBean commitOnRemoteRepo(JsonNode param) {
		// TODO Auto-generated method stub
		return null;
	}
}
