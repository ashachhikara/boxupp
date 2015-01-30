package com.boxupp.utilities;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

public class LocalRepoUtilities {

	private static LocalRepoUtilities localRepoUtilities = null;
	  private String localPath, remotePath;
	    private Repository localRepo;
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
	        localPath = "/tmp/repos/mytest";
	        remotePath = "git@github.com:me/mytestrepo.git";
	        localRepo = new FileRepository(localPath + "/.git");
	        git = new Git(localRepo);
	       // LocalRepoUtilities.getInstance().testCreate();
	      
	        try {
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

	   
	    public void testClone() throws IOException, GitAPIException {
	        Git.cloneRepository().setURI(remotePath)
	                .setDirectory(new File(localPath)).call();
	    }

	    public void testAdd() throws IOException, GitAPIException {
	        File myfile = new File(localPath + "/Vagrantfile");
	        myfile.createNewFile();
	        git.add().addFilepattern("Vagrantfile").call();
	    }


	    public void testCommit() throws IOException, GitAPIException,
	            JGitInternalException {
	        git.commit().setMessage("Added myfile").call();
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
}
