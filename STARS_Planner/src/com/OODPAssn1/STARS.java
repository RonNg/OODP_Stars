package com.OODPAssn1;

import com.OODPAssn1.Entities.*;
import com.OODPAssn1.Managers.CourseManager;
import com.OODPAssn1.Managers.UserManager;

import javax.mail.internet.InternetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;


/**
 * Created by jonah on 15/3/2017.
 */
public class STARS
{
	private static STARS instance;

	private boolean isInitAlready = false;
	private UserManager userManager;
	private CourseManager courseManager;
	private User currentLogOnUser;
	private AccessPeriod currentAccessPeriod;
	private Notification studentNotification;


	private STARS (int i)
	{
		init();
	}//Not sure why constructor require argument for it to be recognize

	public static STARS getInstance ()
	{
		if (instance == null)
		{
			instance = new STARS(1);
			return instance;
		}
		return instance;
	}

	public int init ()
	{
		if (isInitAlready)
		{
			System.out.println("STARS is being reinitialised again!");
			return -1;
		}


		userManager = UserManager.getInstance();
		courseManager = CourseManager.getInstance();
		studentNotification = new Notification();

		return 1;

	}


//--------------------------Method for login to Stars---------------------------------------
/*
  - Return 1 for student and 0 for admin to facilitate displaying of
	corresponding menu.
  - Return -1 if User not found in database or failed authentication
  - Invoke authenticateUser method in UserManager class for authentication process
  - Receive and store the logged-on User object to keep track of logged-on user identity
*/

	public int loginToStars(String userName, String password)
	{

		User user = userManager.authenticateUser(userName, password);
		if (user != null) //Login successful
		{
			//System.out.println(user.getType() + " " + user.getUsername() + " is now logged on to Stars!");
			currentLogOnUser = user;
			if(user.getType()== User.USER_TYPE.STUDENT)
				return 0;
			else
				return 1;
		}
		return -1;
	}

	public String validateStudentLogin (String username, String password)
	{
		User user = userManager.authenticateUser(username, password);
		if (user != null && user instanceof Student) //Login successful
		{
			return ((Student) user).getMatricNo();
		}
		else
			return null;
	}

//------------------------Method to check current date is within access period-----------------------------

	public boolean checkAccessPeriod ()
	{

		//SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
		Calendar currentDate = Calendar.getInstance();
		//System.out.println("Current date:" + dateFormat.format(currentDate.getTime()));
		//System.out.println("Start date: " + dateFormat.format(startDate.getTime()) + "   " + "End date: " + dateFormat.format(endDate.getTime()));
		return currentDate.after(userManager.getAccessPeriod().getStartDate()) && currentDate.before(userManager.getAccessPeriod().getEndDate());


	}
//----------------------------Method to check valid Date format---------------------------------------------------------

	public boolean checkDateFormat (String date)
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			sdf.setLenient(false);
			Date dateObj = sdf.parse(date);
		} catch (Exception e)
		{
			return false;
		}
		return true;
	}

	public boolean checkStartDateCompatibility (String start)
	{

		Date startDate;
		Date currentDate = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

		try
		{
			startDate = new SimpleDateFormat("dd/MM/yyyy").parse(start);


		} catch (ParseException e)
		{
			return false;
		}

		return (startDate.after(currentDate) || fmt.format(currentDate).equals(fmt.format(startDate)));
	}

	public boolean checkEndDateCompatibility (String start, String end)
	{

		Date startDate;
		Date endDate;
		try
		{
			startDate = new SimpleDateFormat("dd/MM/yyyy").parse(start);
			endDate = new SimpleDateFormat("dd/MM/yyyy").parse(end);

		} catch (ParseException e)
		{
			return false;
		}
		return endDate.after(startDate);
	}

//----------------------------Method to set access period---------------------------------------------------------------

	public String setAccessPeriod (String start, String end)
	{

		Date startDateObj, endDateObj;
		Calendar startDateCal = Calendar.getInstance();
		Calendar endDateCal = Calendar.getInstance();
		String startDate, endDate;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");


		try
		{
			startDateObj = new SimpleDateFormat("dd/MM/yyyy").parse(start);
			System.out.println(startDateObj);
			startDateCal.setTime(startDateObj);
			endDateObj = new SimpleDateFormat("dd/MM/yyyy").parse(end);
			System.out.println(endDateObj);
			endDateCal.setTime(endDateObj);

		} catch (ParseException e)
		{
			System.out.println("Please enter format as shown!");
		}


		if (userManager.changeAccessPeriod(startDateCal, endDateCal))
		{

			this.saveData();
			return "Start date: " + sdf.format(startDateCal.getTime()) + "   " + "End date: " + sdf.format(endDateCal.getTime());
		}
		else
			return "Error in setting access period!";


	}

//----------------------------Method to get access period as formatted string-------------------------------------------

	public String getAccessPeriod ()
	{

		String rtrStr;
		AccessPeriod accessPeriod;
		accessPeriod = userManager.getAccessPeriod();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");

		rtrStr = "Start date: " + sdf.format(accessPeriod.getStartDate().getTime()) + "   " +
				"End date: " + sdf.format(accessPeriod.getEndDate().getTime());
		return rtrStr;
	}

	public boolean doesCourseExist (String courseId)
	{
		List<Course> courseList = courseManager.getCourseList();
		for (int i = 0; i < courseList.size(); ++i)
		{

			//System.out.println(courseList.get(i).getCourseId());
			if (courseList.get(i).getCourseId().equalsIgnoreCase(courseId))
			{
				return true;
			}
		}
		return false;
	}

	public boolean doesIndexExist (int indexNo)
	{
		List<Course> courseList = courseManager.getCourseList();
		for (int i = 0; i < courseList.size(); ++i)
		{
			List<Index> indexList = courseList.get(i).getIndexList();
			if (indexList == null)
				continue; //Go to next course

			for (int j = 0; j < indexList.size(); ++j)
			{
				if (indexList.get(j).getIndexNum() == indexNo)
					return true; //index number is found in entire course list
			}
		}
		return false;

	}

	public String checkIfIndexClash (Index index1, Index index2)
	{
		//Check Lecture clash
		Course course1 = courseManager.getCourseByIndexNo(index1.getIndexNum());
		Course course2 = courseManager.getCourseByIndexNo(index2.getIndexNum());

		List<TimeSlot> course1LectList = course1.getLecTimeSlotList();
		List<TimeSlot> course2LectList = course2.getLecTimeSlotList();


		if (course1.getCourseId().equals(course2.getCourseId()))
			return "SAME COURSE";

		for (int i = 0; i < course1LectList.size(); ++i)
		{
			for (int x = 0; x < course2LectList.size(); ++x)
			{
				//If the days match
				if (course1LectList.get(i).getDay().equals(course2LectList.get(x).getDay()))
				{
					//if Course 2's Lect start time is in between Course 1's Lecture time, there's a clash
					int course1StartTime = Integer.parseInt(course1LectList.get(i).getStartTime().substring(0, 2) + course1LectList.get(i).getStartTime().substring(3));
					int course1EndTime = Integer.parseInt(course1LectList.get(i).getEndTime().substring(0, 2) + course1LectList.get(i).getEndTime().substring(3));
					int course2StartTime = Integer.parseInt(course2LectList.get(x).getStartTime().substring(0, 2) + course2LectList.get(x).getStartTime().substring(3));
					int course2EndTime = Integer.parseInt(course2LectList.get(x).getEndTime().substring(0, 2) + course2LectList.get(x).getEndTime().substring(3));


					//Start time must check if equals. If both start time is 1400, it clashes.
					//But if start time of second course is 1400 but end time of first course is 1400, it doesn't clash
					//Students can just run to their next class
					if (course2StartTime >= course1StartTime && course2StartTime < course1EndTime)
					{
						String retStr = "";
						retStr += course1.getCourseId() + " lecture time clashes with " + course2.getCourseId() + " on " + course1LectList.get(i).getDay().toString()
								+ "\n\n" + course1.getCourseId() + " lecture time: " + course1StartTime + " - " + course1EndTime + "\n"
								+ course2.getCourseId() + " lecture time: " + course2StartTime + " - " + course2EndTime + "\n";

						return retStr;
					}
				}
			}
		}

		//Check Tut clash_
		List<TimeSlot> index1TutLabList = index1.getTutLabTimeSlotList();
		List<TimeSlot> index2TutLabList = index2.getTutLabTimeSlotList();
		for (int i = 0; i < index1TutLabList.size(); ++i)
		{
			for (int x = 0; x < index2TutLabList.size(); ++x)
			{
				//Will only clash if same day
				if (index1TutLabList.get(i).getDay().equals(index2TutLabList.get(x).getDay()))
				{
					//if Index 2's Tut start time is in between Index 1's Tut time, there's a clash
					int index1TutLabStartTime = Integer.parseInt(index1TutLabList.get(i).getStartTime().substring(0, 2) + index1TutLabList.get(i).getStartTime().substring(3));
					int index1TutLabEndTime = Integer.parseInt(index1TutLabList.get(i).getEndTime().substring(0, 2) + index1TutLabList.get(i).getEndTime().substring(3));

					int index2TutLabStartTime = Integer.parseInt(index2TutLabList.get(x).getStartTime().substring(0, 2) + index2TutLabList.get(x).getStartTime().substring(3));
					int index2TutLabEndTime = Integer.parseInt(index2TutLabList.get(x).getEndTime().substring(0, 2) + index2TutLabList.get(x).getEndTime().substring(3));

					if (index2TutLabStartTime >= index1TutLabStartTime && index2TutLabStartTime < index1TutLabEndTime)
					{
						String retStr = "";
						retStr += "Index " + index1.getIndexNum() + " LAB/TUT time clashes with " + " Index " + index2.getIndexNum() + " on " + index1TutLabList.get(i).getDay().toString()
								+ "\n\n" + course1.getCourseName() + " " + index1TutLabList.get(i).getType() + " time: " + index1TutLabStartTime + " - " + index1TutLabEndTime + "\n"
								+ course2.getCourseName() + " " + index2TutLabList.get(x).getType() + " time: " + index2TutLabStartTime + " - " + index2TutLabEndTime + "\n";

						return retStr;
					}
				}
			}
		}
		return "NO CLASH";
	}


	/**
	 * @param indexNo  Index number to check
	 * @param matricNo Leave empty if matric no to check is the current user's matric no
	 * @return -1 Error <br>
	 * 0 if student is not enrolled in this index <br>
	 * 1 if student is enrolled in this index
	 */
	public int checkIfEnrolled (int indexNo, String matricNo)
	{
		Index tempIndex = courseManager.getIndexByIndexNo(indexNo);

		if (tempIndex == null)
			return -1; //Index does not exist

		if (matricNo == "")
		{
			if (currentLogOnUser instanceof Student)
			{
				Student currLoggedStudent = (Student) currentLogOnUser;

				if (tempIndex.checkIfStudentEnrolled(currLoggedStudent.getMatricNo()))
					return 1;
				else
					return 0;
			}
			else
				return -1;
		}
		else
		{
			if (tempIndex.checkIfStudentEnrolled(matricNo))
				return 1;
			else
				return 0;
		}

	}

	/**
	 * @param indexNo  Index number to check
	 * @param matricNo Leave empty if matric no to check is the current user's matric no
	 * @return -1 Error <br>
	 * 0 if student is not in waitlist of  in this index <br>
	 * 1 if student is in waitlist of in this index
	 */
	public int checkIfInWaitList(int indexNo, String matricNo){
		Index tempIndex = courseManager.getIndexByIndexNo(indexNo);

		if (tempIndex == null)
			return -1; //Index does not exist

		if (matricNo == "")
		{
			if (currentLogOnUser instanceof Student)
			{
				Student currLoggedStudent = (Student) currentLogOnUser;

				if (tempIndex.checkIfStudentInWaitList(currLoggedStudent.getMatricNo()))
					return 1;
				else
					return 0;
			}
			else
				return -1;
		}
		else
		{
			if (tempIndex.checkIfStudentInWaitList(matricNo))
				return 1;
			else
				return 0;
		}
	}

	public boolean checkIfIndexExists (int indexNo)
	{
		if (courseManager.getIndexByIndexNo(indexNo) != null)
			return true;
		else
			return false;
	}

	public boolean checkIfIndexIsInCourse(int indexNo, String courseID){
		return courseManager.getCourseByCourseId(courseID).equals(courseManager.getCourseByIndexNo(indexNo));
	}

	public boolean areIndexSameCourse (int indexNo1, int indexNo2)
	{
		if (courseManager.getCourseByIndexNo(indexNo1) == courseManager.getCourseByIndexNo(indexNo2))
			return true;
		else
			return false;
	}

	  /*==================================================


					   STUDENT METHODS

	 ==================================================*/

	/**
	 * @param indexNo The waitlist's index
	 * @return returns an int array of size 2 <br>
	 * The first value in the array is the Student's position in the waitlist
	 * The second value in the array is the total number of Students in the waitlist
	 */
	public int[] student_getPositionInWaitlist (int indexNo)
	{
		if (doesIndexExist(indexNo) == false || currentLogOnUser.getType() != User.USER_TYPE.STUDENT)
			return null;

		int[] returnPos = new int[2];

		List<String> tempWaitList = courseManager.getIndexByIndexNo(indexNo).getWaitList();

		for (int i = 0; i < tempWaitList.size(); ++i)
		{
			if (((Student) currentLogOnUser).getMatricNo() == tempWaitList.get(i))
			{
				returnPos[0] = i + 1; //+1 since index starts at 0. If you're at index 0, you're in fact in position 1.
				returnPos[1] = tempWaitList.size();
				return returnPos;
			}
		}
		return null; //Student is not in the wait list.
	}

	/**
	 * @param indexNo Index Number to enrol into
	 * @return Error occured -> 0 <br>
	 * Added into waitlist -> -1 <br>
	 * Successfully enrolled into index -> 1 <br>
	 * Already enrolled in index -> 2 <br>
	 * Already in waitlist of the index -> 3 <br>
	 * Index Clash -> 4 <br>
	 * Already in another index of the same course -> 5 <br>
	 * Succesfully switched index -> 6 <br>
	 * Switched index but added into waitlist of the target index -> 7
	 *
	 * Error = 0
	 * Success = 1
	 * Success(In waitlist) = 2
	 * Failed(Already in Index) = 3
	 * Failed(Already in Index waitlist) = 4
	 * Failed(Already in another Index of the same course) = 5
	 * Failed(Already in wait list of another Index of the same course) = 6
	 * Failed(Clash timing) = 7

	 */
	public int student_EnrolIndex (int indexNo)
	{
		if (currentLogOnUser.getType() != User.USER_TYPE.STUDENT) //only students can enroll into indexes
			return 0;

		Student tempStud = (Student) currentLogOnUser;


		//Check if we can switch index
		Index indexToJoin = courseManager.getIndexByIndexNo(indexNo);
		Index indexInSameCourse = null;

		//Is the current student trying ot switch from waitlist (of another index) to indexToJoin (i.e. waitlist switch to index)
		boolean switchInWaitList = false;
		Course courseOfIndexToJoin = courseManager.getCourseByIndexNo(indexToJoin.getIndexNum());

		if(indexToJoin.checkIfStudentEnrolled(tempStud.getMatricNo()))
			return 3; // If student enrolled in the index return 3
		if(indexToJoin.checkIfStudentInWaitList(tempStud.getMatricNo()))
			return 4; // If student in waitlist of index return 4

		List<Integer> studEnrolledIndexList = tempStud.getCourseIndexList();
		for(int n = 0; n < studEnrolledIndexList.size(); n++){
			Course toCheck = courseManager.getCourseByIndexNo(studEnrolledIndexList.get(n));
			if(toCheck.equals(courseOfIndexToJoin)){
				if(studEnrolledIndexList.get(n) == indexNo)
					return 3; // If student enrolled in the index return 3
				else
					return 5; // If student enrolled in another index of the same course return 5
			}
		}

		List<Index> indexOfCourseToJoinList = courseOfIndexToJoin.getIndexList();
		for(int n = 0; n < indexOfCourseToJoinList.size(); n++){
			Index toCheck = indexOfCourseToJoinList.get(n);
			if(toCheck.checkIfStudentInWaitList(tempStud.getMatricNo())){
				if(toCheck.equals(indexToJoin)){
					return 4; // If student in waitlist of index return 4
				}else{
					return 6; // If student in waitlist of another index of the same course return 6
				}
			}
		}

		for (int i = 0; i < studEnrolledIndexList.size(); ++i) {
			Index currUserIndex = courseManager.getIndexByIndexNo(studEnrolledIndexList.get(i));

			String result = checkIfIndexClash(currUserIndex, indexToJoin);
			if (result.equals("NO CLASH") == false)
			{
				if (result.equals("SAME COURSE"))
					return 3; // If student enrolled in the index return 3
				return 7; // If timing clash with other index of the student return 7
			}

		}

		switch(courseManager.enrolInIndex(tempStud.getMatricNo(),indexNo)){
			case -1:
				return 2;
			case 0:
				return 0;
			case 1:
				tempStud.addCourseIndex(indexNo);
				return 1;
			case 2:
				return 3;
			case 3:
				return 4;
			default:
				return 0;

		}
		/*
		// UNTIL HERE
		//Loops through to find if student is enrolled or in waitlist in the same CourseID of the index he wants to join
		for (int i = 0; i < courseOfIndexToJoin.getIndexList().size(); ++i)
		{
			Index tempIndex = courseOfIndexToJoin.getIndexList().get(i);

			if (indexToJoin.getIndexNum() == tempIndex.getIndexNum())
			{
				//You're attempting to join the same index
				if (indexToJoin.checkIfStudentInWaitList(tempStud.getMatricNo()))
					return 111; //You are attempting to join the same wailist
				else if( indexToJoin.checkIfStudentEnrolled(tempStud.getMatricNo()))
					return 2; //Already enrolled in the index
			}

			if (tempIndex.checkIfStudentEnrolled(tempStud.getMatricNo()))
			{
				indexInSameCourse = tempIndex;
			}
			else if (tempIndex.checkIfStudentInWaitList(tempStud.getMatricNo()))
			{
				indexInSameCourse = tempIndex;
				switchInWaitList = true;
			}
		}


		//Detect if student is joining another index of the same course
		if (indexInSameCourse != null)
		{
			int switchResult = student_SwitchIndex(indexInSameCourse.getIndexNum(), indexToJoin.getIndexNum(), switchInWaitList);
			switch (switchResult)
			{
				case 1:
					saveData();
					return 6;
				case -1:
					saveData();
					return 7;
				default:
					return switchResult;
			}
		}


		//This code chunk checks for Index Clash
		List<Integer> currentUserIndexList = tempStud.getCourseIndexList();

		/*===================================
					CHECK CLASH
		 ===================================*/
		//Checks every single index in the current user against the index to join to see if there is a clash

		/*for (int i = 0; i < currentUserIndexList.size(); ++i)
		{
			Index currUserIndex = courseManager.getIndexByIndexNo(currentUserIndexList.get(i));

			//TODO: Switch index if same course and got vacancy. Need to check if student is in waitlist of current course also
			String result = checkIfIndexClash(currUserIndex, indexToJoin);
			if (result.equals("NO CLASH") == false)
			{
				if (result.equals("SAME COURSE"))
					return 5; //SAME COURSE

				return 4; //Index clashes
			}

		}

		//If it goes beyond this point, it means its not an index switch/index doesn't clash.
		//Enroll into course
		int result = courseManager.enrolInIndex(tempStud.getMatricNo(), indexNo);

		switch (result)
		{
			 /*======================================
					 ADDED INTO INDEX/WAITLIST
			=======================================*//*
			//Successfully enrolled
			case 1:
				//Enroll into student index list
				tempStud.addCourseIndex(indexNo); //Student is enrolled in Index list. Update student's course index information
				break;
			//Added into waitlist
			case -1:
				break;
			//Cases 0, 2 and 3 do not do anything as nothing is being changed in CourseManager. See error code.

		}
		saveData();*/
	}

	/**
	 * Withdraw from the Index number specified by the current logged in student
	 *
	 * @param indexNo
	 * @return returns 1 if index succesfully dropped <br>
	 * returns 0 if error occured
	 */
	public int student_DropIndex (int indexNo)
	{
		if (currentLogOnUser.getType() != User.USER_TYPE.STUDENT) //only students can enroll into indexes
			return 0;

		Student currentLogOnStud = (Student) currentLogOnUser;

		//Remove student from index in CourseManager
		String[] result = courseManager.dropFromIndex(currentLogOnStud.getMatricNo(), indexNo, false);


		if (result[0] != "ERROR")
		{
			//Remove index from Student
			currentLogOnStud.getCourseIndexList().remove((Integer) indexNo);

			if (result[0] == "SUCCESS")
			{
				saveData();
				return 1;
			}

			//HANDLE means that a student from the start of the waitlist has been added into the course index in CourseManager.
			//STARS has to update the UserManager side
			//Remove index from student
			if (result[0] == "HANDLE")
			{
				Student tempStudent = userManager.getStudentByMatricNo(result[1]);
				tempStudent.addCourseIndex(Integer.parseInt(result[2]));

				String courseId = courseManager.getCourseByIndexNo(indexNo).getCourseId();
				String courseName = courseManager.getCourseByIndexNo(indexNo).getCourseName();
				String subject = "Successfully enrolled into Index " + indexNo;
				String message = "Dear " + tempStudent.getName() + ",\n\nAs a student has withdrawn from the index, you have been removed from the waitlist and enrolled into the Index " + indexNo
						+ " for " + courseId + " - " + courseName;

				List<Integer> sEnrollIndex = tempStudent.getCourseIndexList();
				Index indexJoined = courseManager.getIndexByIndexNo(indexNo);
				for (int i = 0; i < sEnrollIndex.size(); ++i) {
					Index currUserIndex = courseManager.getIndexByIndexNo(sEnrollIndex.get(i));
					String cClash = checkIfIndexClash(currUserIndex, indexJoined);
					if (!result.equals("NO CLASH"))
					{
						if (!result.equals("SAME COURSE")){
							student_DropIndex(indexNo);
							subject = "Failed to enrol into Index " + indexNo;
							message = "Dear " + tempStudent.getName() + ",\n\nAlthough a student has withdrawn from the index, you have not enrolled into Index " + indexNo + " for " + courseId +
									" as the timing clashed with your current registered Indexes.\nYou have been removed from the waitlist. Please enroll to the Index again.";
						}
					}
				}

				studentNotification.sendMessage(tempStudent.getEmail(), subject, message);
				saveData();
				return 1;
			}
		}

		return 0;
	}

	public int student_SwapIndex (int firstIndex, String secondMatric, int secondIndex)
	{
		String firstMatric = ((Student) currentLogOnUser).getMatricNo();

		//We need to do some evil stuff and bypass waitlist handling
		courseManager.dropFromIndex(firstMatric, firstIndex, true);
		userManager.getStudentByMatricNo(firstMatric).getCourseIndexList().remove((Integer) firstIndex);

		courseManager.dropFromIndex(secondMatric, secondIndex, true);
		userManager.getStudentByMatricNo(secondMatric).getCourseIndexList().remove((Integer) secondIndex);

		//SWAPPED!
		if (courseManager.enrolInIndex(firstMatric, secondIndex) == 1)
		{
			userManager.getStudentByMatricNo(firstMatric).getCourseIndexList().add((Integer) secondIndex);
			if (courseManager.enrolInIndex(secondMatric, firstIndex) == 1)
			{
				userManager.getStudentByMatricNo(secondMatric).getCourseIndexList().add((Integer) firstIndex);
				saveData();
				return 1;
			}
		}

		return 0; //Failed to swap index

	}


	/**
	 * @param currentIndexNo Index Number to de-enrol
	 * @param targetIndexNo Index Number to de-enrol
	 * @return Serious Error occured -> -1 <br>
	 * @return Error occured -> 0 <br>
	 * Successfully switched -> 1 <br>
	 * Successfully switch(in waitlist) -> 2 <br>
	 */
	public int student_SwitchIndex (int currentIndexNo, int targetIndexNo)
	{
		Index currentIndex = courseManager.getIndexByIndexNo(currentIndexNo);
		Index targetIndex = courseManager.getIndexByIndexNo(targetIndexNo);
		String currentUserMatric = ((Student) currentLogOnUser).getMatricNo();

		int result = student_EnrolIndex(targetIndexNo);
		switch (result){
			case 1:
			case 2:
				return -1;
			case 5:
				if(student_DropIndex(currentIndexNo)==1){
					switch (student_EnrolIndex(targetIndexNo)) {
						case 0:
							switch (student_EnrolIndex(currentIndexNo)) {
								case 1:
									return 0;
								case 2:
									return 0;
							}
						case 1:
							return 1;
						case 2:
							return 2;
					}
				}
			case 6:
				if(currentIndex.removeStudentFromWaitlist(currentUserMatric)){
					switch (student_EnrolIndex(currentIndexNo)){
						case 1:
							return 1;
						case 2:
							return 2;
					}
				}
		}
		return 0;
/*
		if(result == 0)
			return 0;

		if (switchInWaitList)
			currentIndex.getWaitList().remove(currentUserMatric); //Remove current student from waitlist
		else
			currentIndex.withdrawStudent(currentUserMatric, false);

		switch (result) //If added into index
		{
			case 1: //Succesfully added
				((Student) currentLogOnUser).getCourseIndexList().remove((Integer)currentIndexNo); //Removes the currentIndex
				((Student) currentLogOnUser).addCourseIndex(targetIndexNo); //Adds the target index
				//Do for coursemanager side
				return 1;

			case -1: //added into waitlist
				return -1;
		}

		return 0; //error occured*/
	}



	/*==================================================


					   ADMIN METHODS


	 ==================================================*/

//--------------------------------------Method to delete course from STARS----------------------------------------------
/*
1) Check for existance of course
2) Check if course contain index that are registered with students
	- Get course obj
	- Get indexList
	- Loop through indexList to get the studentsEnrolledList
	- If size > 0, run another loop to retrieve student obj to remove index id from it's courseIndexList
	- Save changes
3) Remove course obj from courseList
	- Save changes
4) Return string that contain names of student that are de-enrolled due to deletion
 */


	public String printStudentsInCourse (String courseId)
	{

		String retStr = "";
		String courseNotFoundErr = "Error! Course not found!";
		String deleteCourseErr = "Error in deletion of course!";

		//if course does not exists
		if (!this.doesCourseExist(courseId))
			return courseNotFoundErr;

		Course course = courseManager.getCourseByCourseId(courseId);

		Index index = null;
		Student student = null;
		List<Index> indexList = course.getIndexList();
		String matricNo; // Stores matrix number

		if (indexList.size() != 0)
		{//Check if Course contain index

			for (int i = 0; i < indexList.size(); i++)
			{

				index = indexList.get(i);
				//- Loop through indexList to get the studentsEnrolledList
				//- If size > 0, run another loop to retrieve student obj to remove index id from it's courseIndexList
				if (index.getEnrolledStudentList().size() > 0)
				{

					for (int j = 0; j < index.getEnrolledStudentList().size(); j++)
					{
						matricNo = index.getEnrolledStudentList().get(j);
						student = userManager.getStudentByMatricNo(matricNo);
						student.removeCourseIndex(index.getIndexNum());
						retStr = retStr + "Matric no.: " + matricNo + "    Name:" + student.getName() +
								"    From Index: " + index.getIndexNum() + "\n";
					}
					userManager.save();
				}
			}
		}
		if (!courseManager.deleteCourse(course))
			retStr = deleteCourseErr;
		else
			courseManager.save();

		return retStr;
	}

	public String deleteCourseViaCourseId (String cId)
	{

		String retStr = "";
		String courseNotFoundErr = "Error! Course not found!";
		String deleteCourseErr = "Error in deletion of course!";
		//if course does not exists
		if (!this.doesCourseExist(cId))
			return courseNotFoundErr;
		Course course = courseManager.getCourseByCourseId(cId);
		Index index = null;
		Student student = null;
		List<Index> indexList = course.getIndexList();
		String matricNo; // Stores matrix number

		if (indexList.size() != 0)
		{//Check if Course contain index

			for (int i = 0; i < indexList.size(); i++)
			{

				index = indexList.get(i);
				//- Loop through indexList to get the studentsEnrolledList
				//- If size > 0, run another loop to retrieve student obj to remove index id from it's courseIndexList
				if (index.getEnrolledStudentList().size() > 0)
				{

					for (int j = 0; j < index.getEnrolledStudentList().size(); j++)
					{
						matricNo = index.getEnrolledStudentList().get(j);
						student = userManager.getStudentByMatricNo(matricNo);
						student.removeCourseIndex(index.getIndexNum());
						retStr = retStr + "Matric no.: " + matricNo + "    Name:" + student.getName() +
								"    From Index: " + index.getIndexNum() + "\n";
					}
					userManager.save();
				}
			}
		}
		if (!courseManager.deleteCourse(course))
			retStr = deleteCourseErr;
		else
			courseManager.save();

		return retStr;
	}


	/*==================================================


					   ADMIN METHODS


	 ==================================================*/


//------------------------Method for adding/updating of Course----------------------------------------------------------


	public boolean admin_AddCourse (String courseId, String courseName, String faculty)
	{
		boolean success = courseManager.addCourse(courseId, courseName, faculty);
		if (success)
			courseManager.save(); //Save after adding
		return success;
	}

	
    public boolean admin_AddLecTimeSlot(String courseId, String timeSlotDayStr, int startTimeHH, int startTimeMM, int endTimeHH, int endTimeMM, String locationLT)
	{
		Course course = courseManager.getCourseByCourseId(courseId);
		TimeSlot.DAY timeSlotDay = TimeSlot.DAY.valueOf(timeSlotDayStr);
		List<TimeSlot> tempTutLabList;
		if (course != null)
		{
			//Save after adding
			for(int i = 0; i < course.getIndexList().size(); i++){

				tempTutLabList = course.getIndexList().get(i).getTutLabTimeSlotList();
				for(int j = 0; j < tempTutLabList.size(); j++) {
					if (tempTutLabList.get(j).getDay() == timeSlotDay)
						if(tempTutLabList.get(j).getStartTime().equals(LocalTime.of(startTimeHH, startTimeMM).toString()))
						return false;
				}
			}

			if(course.addLecTimeSlot(timeSlotDay, startTimeHH, startTimeMM, endTimeHH, endTimeMM, locationLT)) {
				courseManager.save();
				return true;
			}else return false;


		}
		else
		{
			System.out.println("STARS: admin_AddLecTimeSlot CourseID not found");
			return false;
		}
	}

	public boolean admin_AddIndex (String courseId, int indexNoToAdd, int maxStudent)
	{
		boolean alreadyExists = false;
		Course tempCourse = courseManager.getCourseByCourseId(courseId);

		//Check whether this index already exists
		List<Index> courseIndexList = tempCourse.getIndexList();
		for (int z = 0; z < courseIndexList.size(); ++z)
		{
			//Index number already exists
			if (courseIndexList.get(z).getIndexNum() == indexNoToAdd)
				return false;
		}

		boolean success = tempCourse.addIndex(indexNoToAdd, maxStudent);

		if (success)
		{
			//Save after adding
			courseManager.save();
			return true;
		}

		return false;
	}

	public boolean admin_AddIndexLabTimeSlot(int indexNo, String dayStr, int startH, int startM, int endH, int endM, String labLocation)
	{
		Index tempIndex = courseManager.getIndexByIndexNo(indexNo);
		//Can't find the index
		if (tempIndex == null)
			return false;
		TimeSlot.DAY day = TimeSlot.DAY.valueOf(dayStr);
		List<TimeSlot> lecTime = courseManager.getLecTimeSlot(courseManager.getCourseByIndexNo(indexNo));
		for(int i = 0; i < lecTime.size(); i++){
			if(lecTime.get(i).getDay() == day) {
				if (lecTime.get(i).getStartTime().equals(LocalTime.of(startH, startM).toString()))
					return false;
			}
		}
		boolean success = tempIndex.addLabTimeSlot(day, startH, startM, endH, endM, labLocation);

		if (success)
		{
			courseManager.save();
			return true;
		}

		return true;
	}

	public boolean admin_AddIndexTutTimeSlot(int indexNo, String dayStr, int startH, int startM, int endH, int endM, String tutLocation)
	{
		Index tempIndex = courseManager.getIndexByIndexNo(indexNo);
		//Can't find the index
		if (tempIndex == null)
			return false;
		TimeSlot.DAY day = TimeSlot.DAY.valueOf(dayStr);
		List<TimeSlot> lecTime = courseManager.getLecTimeSlot(courseManager.getCourseByIndexNo(indexNo));
		for(int i = 0; i < lecTime.size(); i++){
			if(lecTime.get(i).getDay() == day) {
				if (lecTime.get(i).getStartTime().equals(LocalTime.of(startH, startM).toString()))
					return false;
			}
		}
		boolean success = tempIndex.addTutTimeSlot(day, startH, startM, endH, endM, tutLocation);

		if (success)
		{
			courseManager.save();
			return true;
		}

		return false;
	}

	public String admin_GetLecTimeList(String courseId){
		List<TimeSlot> temp = courseManager.getLecTimeSlot(courseManager.getCourseByCourseId(courseId));
		String output = "";
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb,Locale.ENGLISH);
		for (int n = 0; n < temp.size(); n++)
		{
			TimeSlot tempTimeSlot = temp.get(n);
			sb = new StringBuilder();
			formatter = new Formatter(sb, Locale.ENGLISH);
			formatter.format("%4d)  %-5s | %-5s | %s-%-7s | %s %n", n+1, tempTimeSlot.getType(), tempTimeSlot.getDay().toString(), tempTimeSlot.getStartTime(), tempTimeSlot.getEndTime(), tempTimeSlot.getLocation());
			output += sb.toString();
		}
		return output;
	}

	public boolean admin_DeleteLecTimeSlot(String courseId, int lecSelect){
		Course tempCourse = courseManager.getCourseByCourseId(courseId);
		List<TimeSlot> tempTsList = courseManager.getLecTimeSlot(tempCourse);
		boolean success = false;
		try{
			success =courseManager.deleteLecTimeSlot(tempCourse,tempTsList.get(lecSelect-1));
		}catch (Exception e){
			return false;
		}
		return success;
	}
	
	public String admin_GetLabTutList(String courseId, int indexToGet){
		String output = "";
		Course tempCourse = courseManager.getCourseByCourseId(courseId);
		if(indexToGet == -1){
			return "Unable to find Index.";
		}
		Index tempIndex = tempCourse.getIndex(indexToGet);
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb,Locale.ENGLISH);
		List<TimeSlot> tsList = tempIndex.getTutLabTimeSlotList();
		for(int n = 0; n < tsList.size(); n++){
			TimeSlot tempTimeSlot = tsList.get(n);
			sb = new StringBuilder();
			formatter = new Formatter(sb, Locale.ENGLISH);
			formatter.format("%4d) %-5s | %-5s | %s-%-7s | %s %n", n+1, tempTimeSlot.getType(), tempTimeSlot.getDay().toString(), tempTimeSlot.getStartTime(), tempTimeSlot.getEndTime(), tempTimeSlot.getLocation());
			output += sb.toString();
		}
		return output;
	}

	public boolean admin_DeleteLabTutTimeSlot(String courseId, int indexToGet, int choice){
		Course tempCourse = courseManager.getCourseByCourseId(courseId);
		Index tempIndex = tempCourse.getIndex(indexToGet);
		return tempIndex.deleteTutLabTimeSlot(tempIndex.getTutLabTimeSlotList().get(choice));
	}


	//------------------------------------Method to delete index from course------------------------------------------------
/*
1) Check for existance of index
2) Check if index are registered with students
	- Get index obj
	- get the studentsEnrolledList
	- If size > 0, run loop to retrieve student obj using matric no. to remove index id from it's courseIndexList
	- Save changes
3) Remove index obj from indexList in course
	- Find course obj by index
	- Remove index from indexList
	- Save changes
4) Return string that contain names of student that are de-enrolled due to deletion
 */
	public String deleteIndexFromCourse (int indexNo)
	{
		String rtrStr = "";
		String indexNotFoundStr = "Error! Index not found in STARS!";
		String deleteIndexStr = "Error occured while deleting index!";
		List<String> studentsEnrolledList; // Stores matrix number
		Index index = courseManager.getIndexByIndexNo(indexNo);
		Student student;
		Course course;
		String matricNo;
		if (index == null)
			return indexNotFoundStr;

		if (index.getEnrolledStudentList().size() > 0)
		{

			for (int j = 0; j < index.getEnrolledStudentList().size(); j++)
			{
				matricNo = index.getEnrolledStudentList().get(j);
				student = userManager.getStudentByMatricNo(matricNo);
				student.removeCourseIndex(index.getIndexNum());
				rtrStr = rtrStr + "Matric no.: " + matricNo + "    Name:" + student.getName() +
						"    From Index: " + index.getIndexNum() + "\n";
			}
			userManager.save();
		}
		course = courseManager.getCourseByIndexNo(indexNo);
		if (!courseManager.deleteIndex(course, index))
			rtrStr = deleteIndexStr;
		else
			courseManager.save();

		return rtrStr;
	}


//------------------------------------Method to check vacancy of index--------------------------------------------------

	public String checkIndexVacancy (int indexNo)
	{

		String rtrStr = "";
		String indexNotFoundStr = "Error! Index not found in STARS!";
		Index index = courseManager.getIndexByIndexNo(indexNo);
		if (index == null)
			return indexNotFoundStr;
		rtrStr = "Number of vacancy in Index " + indexNo + ": " + index.getNumberOfVacancy() + "/" + index.getMaxNumberOfStudent();
		return rtrStr;
	}

//-------------------------------Method for enrolling of Student into STARS---------------------------------------------
//This method will make use of UserManager to create student and write it into database.

	
    public boolean admin_addStudent(String name, String email, String matricNo,
                                 int contact, String genderStr, String nationality,
                                 String username, String password) {
		Student.GENDER gender = Student.GENDER.valueOf(genderStr);
		return userManager.addStudent(name, email, matricNo, contact, gender,
				nationality, username, password);

	}

	public boolean checkStudentExist (String matricNo)
	{
		if (userManager.getStudentByMatricNo(matricNo) == null)
			return false;
		return true;
	}


//-------------------------------Method to write all list back to database----------------------------------------------

	public boolean saveData ()
	{
		if (userManager.save() && courseManager.save())
		{
			//When we save the data of UserManager, the password is encrypted again. We need to decrypt it for login to work again.
			return true;
		}
		else
			return false;
	}
//------------------------------Method to print all course available for registration-----------------------------------

	public String printCourseList ()
	{
		String output = "";
		StringBuilder outputBuild;
		List<Course> courseList = courseManager.getCourseList();
		if (courseList == null || courseList.size() <= 0)
		{
			return "No Course Found";
		}
		Course temp;
		for (int i = 0; i < courseList.size(); ++i)
		{
			temp = courseList.get(i);
			outputBuild = new StringBuilder();
			Formatter formatter = new Formatter(outputBuild, Locale.ENGLISH);
			formatter.format("%-8s | %-50s | Indexes: ", temp.getCourseId(), temp.getCourseName());
			output += outputBuild.toString();
			List<Index> indexList = temp.getIndexList();
			for (int j = 0; indexList != null && j < indexList.size(); j++)
			{
				if (j < 1)
					output += Integer.toString(indexList.get(j).getIndexNum());
				else
					output += ", " + Integer.toString(indexList.get(j).getIndexNum());
			}
			output += "\n";
		}
		return output;
	}

	public String printCourseDetails(String course){
		if(!doesCourseExist(course))
			return "Course does not exist";
		Course cTemp = courseManager.getCourseByCourseId(course);
		String output = "";
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb,Locale.ENGLISH);
		List<Index> iTemp = cTemp.getIndexList();
		List<TimeSlot> tsTemp = cTemp.getLecTimeSlotList();
		for (int n = 0; n < tsTemp.size(); n++)
		{
			TimeSlot tempTimeSlot = tsTemp.get(n);
			sb = new StringBuilder();
			formatter = new Formatter(sb, Locale.ENGLISH);
			if (n <= 0)
				formatter.format("%-8s | %-8s | %-5s | %-5s | %s-%-7s | %s %n", cTemp.getCourseId(), "", tempTimeSlot.getType(), tempTimeSlot.getDay().toString(), tempTimeSlot.getStartTime(), tempTimeSlot.getEndTime(), tempTimeSlot.getLocation());
			else
				formatter.format("%-8s | %-8s | %-5s | %-5s | %s-%-7s | %s %n", "", "", tempTimeSlot.getType(), tempTimeSlot.getDay().toString(), tempTimeSlot.getStartTime(), tempTimeSlot.getEndTime(), tempTimeSlot.getLocation());
			output += sb.toString();
		}
		for(int m = 0; m < iTemp.size(); m++){
			tsTemp = iTemp.get(m).getTutLabTimeSlotList();
			for(int n = 0; n < tsTemp.size(); n++){
				TimeSlot tempTimeSlot = tsTemp.get(n);
				sb = new StringBuilder();
				formatter = new Formatter(sb, Locale.ENGLISH);
				if(n==0){
					formatter.format("%-8s | %-8d | %-5s | %-5s | %s-%-7s | %s %n", "", iTemp.get(m).getIndexNum(), tempTimeSlot.getType(), tempTimeSlot.getDay().toString(), tempTimeSlot.getStartTime(), tempTimeSlot.getEndTime(), tempTimeSlot.getLocation());
				}else{
					formatter.format("%-8s | %-8s | %-5s | %-5s | %s-%-7s | %s %n", "", "", tempTimeSlot.getType(), tempTimeSlot.getDay().toString(), tempTimeSlot.getStartTime(), tempTimeSlot.getEndTime(), tempTimeSlot.getLocation());
				}

				output += sb.toString();
			}
		}
		return output;
	}


   /*==================================================

					   PRINT METHODS

	 ==================================================*/

//------------------------Method to print list of students enrolled in index--------------------------------------------

	public String getStudentsInIndex (int indexNo)
	{

		String indexNotFoundStr = "Error! Index not found in system!";
		String indexIsEmptyStr = "No student has enrolled to index yet.";
		String retStr = "";
		Student student;

		Index index = courseManager.getIndexByIndexNo(indexNo);
		if (index == null)
			return indexNotFoundStr;

		List<String> indexList = index.getEnrolledStudentList();
		if (indexList.size() == 0)
			return indexIsEmptyStr;
		for (int i = 0; i < indexList.size(); i++)
		{

			student = userManager.getStudentByMatricNo(indexList.get(i));
			retStr = retStr + "Name: " + student.getName() + "  Gender: " + student.getGender() +
					"  Nationality: " + student.getNationality() + "\n";

		}

		return retStr;

	}


//------------------------Method to print list of students enrolled in course-------------------------------------------

	public String getStudentsInCourse (String courseId)
	{

		String courseNotFoundStr = "Error! Course not found in system!";
		String CourseIsEmptyStr = "No student has enrolled to course yet.";
		String retStr = "";
		Course course = courseManager.getCourseByCourseId(courseId);
		List<Index> indexList = null;
		List<String> currIndexMatricList;
		List<String> enrolledStudentMatricList = new ArrayList<String>();
		Student student;

		int sizeOfIndex = 0;

		if (course == null)
			retStr = courseNotFoundStr;
		else
		{
			indexList = course.getIndexList();
			for (int i = 0; i < indexList.size(); ++i)
			{
				currIndexMatricList = indexList.get(i).getEnrolledStudentList();
				for (int j = 0; j < currIndexMatricList.size(); ++j)
				{
					enrolledStudentMatricList.add(currIndexMatricList.get(j));
				}
			}
		}

		if (enrolledStudentMatricList.size() != 0)
		{
			for (int i = 0; i < enrolledStudentMatricList.size(); i++)
			{
				student = userManager.getStudentByMatricNo(enrolledStudentMatricList.get(i));
				retStr = this.formatStudentInfoRtrStr(student, retStr);


			}
		}
		else
			retStr = CourseIsEmptyStr;

		return retStr;
	}

	public String formatStudentInfoRtrStr (Student s, String retStr)
	{

		return retStr = retStr + "Name: " + s.getName() + "  Gender: " + s.getGender() +
				"  Nationality: " + s.getNationality() + "\n";

	}


	/**
	 * print entire index of a course
	 *
	 * @param courseId courseId to print index
	 */
	public String getIndexListOfCourse (String courseId)
	{
		String retStr = "";
		StringBuilder retStrBuild;
		Formatter formatter;
		List<Index> indexList = courseManager.getIndexList(courseManager.getCourseByCourseId(courseId));

		for (int i = 0; i < indexList.size(); ++i)
		{
			Index tempIndex = indexList.get(i);
			List<TimeSlot> tempList = tempIndex.getTutLabTimeSlotList();
			for (int n = 0; n < tempList.size(); n++)
			{
				TimeSlot tempTimeSlot = tempList.get(n);
				retStrBuild = new StringBuilder();
				formatter = new Formatter(retStrBuild, Locale.ENGLISH);
				if (n == 0)
				{
					formatter.format("%-8d | %-5s | %-5s | %s-%-7s | %s %n", indexList.get(i).getIndexNum(), tempTimeSlot.getType(), tempTimeSlot.getDay().toString(), tempTimeSlot.getStartTime(), tempTimeSlot.getEndTime(), tempTimeSlot.getLocation());
				}
				else
				{
					formatter.format("%-8s | %-5s | %-5s | %s-%-7s | %s %n", "", tempTimeSlot.getType(), tempTimeSlot.getDay().toString(), tempTimeSlot.getStartTime(), tempTimeSlot.getEndTime(), tempTimeSlot.getLocation());
				}

				retStr += retStrBuild.toString();
			}
		}
		return retStr;
		//TODO: Print out details of index e.g Time slot info etc...
	}

	/**
	 * This method is called when you want to receive a formatted string containing timetable of the student for printing purposes.
	 *
	 * @param matricNo Matriculation number of the student. Leave empty if accessed by Student Menu
	 * @return formatted string containing currently registered timetable of the student
	 */
	public String getStudentTimeTable (String matricNo)//For student
	{
		String retStr = "";
		StringBuilder retStrBuild;
		Student tempStud = null;
		Formatter formatter;

		if (currentLogOnUser.getType() == User.USER_TYPE.STUDENT)
		{
			matricNo = ((Student) currentLogOnUser).getMatricNo();
			tempStud = (Student) currentLogOnUser;
		}
		else
		{
			tempStud = (Student) UserManager.getInstance().getStudentByMatricNo(matricNo);
			//Only admin has to handle this. This error will not occur if logged in user is student
			if (tempStud == null)
			{
				retStr = "ERROR";
				return retStr;
			}
		}

		List<Integer> indexList = tempStud.getCourseIndexList();

		//Check to see if student has registered for any courses
		if (indexList.size() <= 0)
		{
			retStr = "You are not registered in any course";
		}

		for (int i = 0; i < indexList.size(); ++i)
		{
			//Get Course by Index No will never return null as the indexList exists in some course
			Course tempCourse = courseManager.getCourseByIndexNo(indexList.get(i));
			Index tempIndex = tempCourse.getIndex(indexList.get(i));
			List<TimeSlot> tempList;
			tempList = tempCourse.getLecTimeSlotList();
			for (int n = 0; n < tempList.size(); n++)
			{
				TimeSlot tempTimeSlot = tempList.get(n);
				retStrBuild = new StringBuilder();
				formatter = new Formatter(retStrBuild, Locale.ENGLISH);
				if (n <= 0)
					formatter.format("%-8s | %-8d | %-5s | %-5s | %s-%-7s | %s %n", tempCourse.getCourseId(), indexList.get(i), tempTimeSlot.getType(), tempTimeSlot.getDay().toString(), tempTimeSlot.getStartTime(), tempTimeSlot.getEndTime(), tempTimeSlot.getLocation());
				else
					formatter.format("%-8s | %-8d | %-5s | %-5s | %s-%-7s | %s %n", "", indexList.get(i), tempTimeSlot.getType(), tempTimeSlot.getDay().toString(), tempTimeSlot.getStartTime(), tempTimeSlot.getEndTime(), tempTimeSlot.getLocation());
				retStr += retStrBuild.toString();
			}
			int lecTimeSlotSize = tempList.size();
			tempList = tempIndex.getTutLabTimeSlotList();
			for (int n = 0; n < tempList.size(); n++)
			{
				TimeSlot tempTimeSlot = tempList.get(n);
				retStrBuild = new StringBuilder();
				formatter = new Formatter(retStrBuild, Locale.ENGLISH);
				if (lecTimeSlotSize == 0)
					formatter.format("%-8s | %-8d | %-5s | %-5s | %s-%-7s | %s %n", tempCourse.getCourseId(), indexList.get(i), tempTimeSlot.getType(), tempTimeSlot.getDay().toString(), tempTimeSlot.getStartTime(), tempTimeSlot.getEndTime(), tempTimeSlot.getLocation());
				else
					formatter.format("%-8s | %-8d | %-5s | %-5s | %s-%-7s | %s %n", "", indexList.get(i), tempTimeSlot.getType(), tempTimeSlot.getDay().toString(), tempTimeSlot.getStartTime(), tempTimeSlot.getEndTime(), tempTimeSlot.getLocation());
				retStr += retStrBuild.toString();
				lecTimeSlotSize = 99;
			}
		}

		return retStr;
	}

	/**
	 * This method is called when you want to receive a formatted string containing currently registered indexes of the student for printing purposes.
	 *
	 * @param matricNo Matriculation number of the student. Leave empty if accessed by Student Menu
	 * @return formatted string containing currently registered indexes of the student
	 */
	public String getStudentRegisteredIndex (String matricNo)//For student
	{
		String retStr = "";
		Student tempStud = null;

		if (currentLogOnUser.getType() == User.USER_TYPE.STUDENT)
		{
			matricNo = ((Student) currentLogOnUser).getMatricNo();
			tempStud = (Student) currentLogOnUser;
		}
		else
		{
			tempStud = (Student) userManager.getStudentByMatricNo(matricNo);
			//Only admin has to handle this. This error will not occur if logged in user is student
			if (tempStud == null)
			{
				retStr = "ERROR";
				return retStr;
			}
		}

		List<Integer> indexList = tempStud.getCourseIndexList();

		//Check to see if student has registered for any courses
		if (indexList.size() <= 0)
		{
			retStr = "You are not registered in any course";
		}

		for (int i = 0; i < indexList.size(); ++i)
		{
			//Get Course by Index No will never return null as the indexList exists in some course


			retStr += "Index: " + indexList.get(i) + " - " + courseManager.getCourseByIndexNo(indexList.get(i)).getCourseName() + "\n";

		}
		return retStr;
	}

//----------------Methods for debugging purposes only, remove for production-------------------------------------------
//Remember to remove import statements for Student and Admin when ready for production

	public void printAllList ()
	{
		System.out.println("===============================");
		userManager.printAllUser();

		System.out.println("\n\n===============================");
		System.out.println(printCourseList());

		System.out.println("\n\n===============================");
		courseManager.printAllIndexDetails();

		System.out.println("\n\n===============================");

	}

	public void populateTestWaitlist ()
	{
		userManager.addStudent("qinghui", "qlai002@e.ntu.edu.sg", "U1111111B", 93874270, Student.GENDER.MALE, "Singaporean", "qinghui", "password");
		userManager.addStudent("ron", "c160144@e.ntu.edu.sg", "U333333B", 93874270, Student.GENDER.MALE, "Singaporean", "c160144", "password");
		//userManager.addAdmin("doug", "doug@e.ntu", "doug123", "doug123");


		//courseManager.addCourse("CE2003", "DSD", "SCE");
		//courseManager.createIndex(courseManager.getCourseByCourseId("CE2003"), 10042, 1);
		//courseManager.createIndex(courseManager.getCourseByCourseId("CE2003"), 10043, 1);


		/*=============================
				Create DSD course
		 =============================*/
		if (courseManager.addCourse("CE2003", "Digital System Design", "SCE"))
		{

			courseManager.createIndex(courseManager.getCourseByCourseId("CE2003"), 10042, 1);
			courseManager.createIndex(courseManager.getCourseByCourseId("CE2003"), 10043, 1);


			courseManager.addLecTimeSlot(courseManager.getCourseByIndexNo(10042), TimeSlot.DAY.MON, 14, 00, 15, 00, "LT4");
			courseManager.getCourseByIndexNo(10042).getIndex(10042).addTutTimeSlot(TimeSlot.DAY.THU, 11, 00, 13, 00, "TR45");
			courseManager.getCourseByIndexNo(10042).getIndex(10042).addLabTimeSlot(TimeSlot.DAY.WED, 11, 00, 15, 00, "LAB 2");

			courseManager.getCourseByIndexNo(10043).getIndex(10043).addTutTimeSlot(TimeSlot.DAY.THU, 11, 00, 13, 00, "TR30");
			courseManager.getCourseByIndexNo(10043).getIndex(10043).addLabTimeSlot(TimeSlot.DAY.WED, 11, 00, 15, 00, "LAB 1");
		}
		else
			System.out.println("Course already exist");


		/*=============================
				Create OODP course
		 =============================*/
		if (courseManager.addCourse("CE2002", "Object Oriented Design & Programming", "SCE"))
		{
			courseManager.createIndex(courseManager.getCourseByCourseId("CE2002"), 10052, 1);
			courseManager.createIndex(courseManager.getCourseByCourseId("CE2002"), 10053, 1);
			courseManager.addLecTimeSlot(courseManager.getCourseByIndexNo(10052), TimeSlot.DAY.MON, 11, 00, 12, 00, "LT4");
			courseManager.getCourseByIndexNo(10052).getIndex(10052).addTutTimeSlot(TimeSlot.DAY.THU, 11, 00, 13, 00, "TR45");
			courseManager.getCourseByIndexNo(10052).getIndex(10052).addLabTimeSlot(TimeSlot.DAY.WED, 11, 00, 15, 00, "LAB 2");
			courseManager.getCourseByIndexNo(10053).getIndex(10053).addTutTimeSlot(TimeSlot.DAY.THU, 11, 00, 13, 00, "TR30");
			courseManager.getCourseByIndexNo(10053).getIndex(10053).addLabTimeSlot(TimeSlot.DAY.WED, 11, 00, 15, 00, "LAB 1");

		}
		else
			System.out.println("Course already exist");


		//Adds bob into CourseManager and UserManager
		courseManager.enrolInIndex("U1111111B", 10042);
		userManager.getStudentByMatricNo("U1111111B").addCourseIndex(10042);

		//Adds ron into waitlist
		courseManager.enrolInIndex("U333333B", 10043);
		userManager.getStudentByMatricNo("U333333B").addCourseIndex(10043);

		saveData();
	}

	public void populateDatabase ()
	{
		String name = "Student";
		String nation = "Nation ";
		List<String> emailList = Arrays.asList("c160144@e.ntu.edu.sg", "qhu003@e.ntu.edu.sg", "qlai002@e.ntu.edu.sg");
		List<Student.GENDER> genderList = Arrays.asList(Student.GENDER.FEMALE, Student.GENDER.MALE);
		for (int n = 1; n <= 15; n++)
		{
			userManager.addStudent(name + n, emailList.get((n % 3)), Integer.toString(n), n, genderList.get(n % 2), nation + n, name + n, name + n);
		}

		userManager.addAdmin("admin", "admin@e.ntu", "admin", "admin");

		Course temphold;

		if (courseManager.addCourse("CE2003", "Digital Systems Design", "SCE"))
		{
			temphold = courseManager.getCourseByCourseId("CE2003");
			courseManager.addLecTimeSlot(temphold, TimeSlot.DAY.FRI, 10, 30, 11, 30, "LT11");
			courseManager.addLecTimeSlot(temphold, TimeSlot.DAY.TUE, 15, 30, 16, 30, "LT11");

			courseManager.createIndex(temphold, 10041, 1);
			courseManager.getIndexByIndexNo(10041).addTutTimeSlot(TimeSlot.DAY.THU, 9, 30, 10, 30, "TR+16");
			courseManager.getIndexByIndexNo(10041).addLabTimeSlot(TimeSlot.DAY.TUE, 8, 30, 10, 30, "HWLAB3");

			courseManager.createIndex(temphold, 10042, 1);
			courseManager.getIndexByIndexNo(10042).addTutTimeSlot(TimeSlot.DAY.THU, 13, 30, 14, 30, "TR+15");
			courseManager.getIndexByIndexNo(10042).addLabTimeSlot(TimeSlot.DAY.WED, 14, 30, 16, 30, "HWLAB3");

		}

		if (courseManager.addCourse("CE2002", "Object Oriented Design and Programming", "SCE"))
		{
			temphold = courseManager.getCourseByCourseId("CE2002");
			courseManager.addLecTimeSlot(temphold, TimeSlot.DAY.THU, 8, 30, 9, 30, "LT2A");
			courseManager.addLecTimeSlot(temphold, TimeSlot.DAY.TUE, 14, 30, 15, 30, "LT2A");

			courseManager.createIndex(temphold, 10031, 10);
			courseManager.getIndexByIndexNo(10031).addTutTimeSlot(TimeSlot.DAY.WED, 10, 30, 11, 30, "TR+37");
			courseManager.getIndexByIndexNo(10031).addLabTimeSlot(TimeSlot.DAY.MON, 14, 30, 16, 30, "SWLAB2");

			courseManager.createIndex(temphold, 10032, 10);
			courseManager.getIndexByIndexNo(10032).addTutTimeSlot(TimeSlot.DAY.MON, 9, 30, 10, 30, "TR+15");
			courseManager.getIndexByIndexNo(10032).addLabTimeSlot(TimeSlot.DAY.WED, 14, 30, 16, 30, "SWLAB2");
		}

		if (courseManager.addCourse("MH1812", "Discrete Mathematics", "SPMS"))
		{
			temphold = courseManager.getCourseByCourseId("MH1812");
			courseManager.addLecTimeSlot(temphold, TimeSlot.DAY.WED, 9, 30, 10, 30, "LT4");
			courseManager.addLecTimeSlot(temphold, TimeSlot.DAY.THU, 16, 30, 17, 30, "LT4");

			courseManager.createIndex(temphold, 10161, 10);
			courseManager.getIndexByIndexNo(10161).addTutTimeSlot(TimeSlot.DAY.THU, 10, 30, 11, 30, "TR+17");
			courseManager.getIndexByIndexNo(10161).addLabTimeSlot(TimeSlot.DAY.TUE, 10, 30, 12, 30, "TR+15");

			courseManager.createIndex(temphold, 10163, 10);
			courseManager.getIndexByIndexNo(10163).addTutTimeSlot(TimeSlot.DAY.THU, 10, 30, 11, 30, "TR+16");
			courseManager.getIndexByIndexNo(10163).addLabTimeSlot(TimeSlot.DAY.WED, 14, 30, 16, 30, "TR+15");
		}

		if (courseManager.addCourse("HW0188", "Engineering Communication I", "HSS"))
		{
			temphold = courseManager.getCourseByCourseId("HW0188");

			courseManager.createIndex(temphold, 15171, 10);
			courseManager.getIndexByIndexNo(15171).addTutTimeSlot(TimeSlot.DAY.MON, 9, 30, 11, 30, "TR+34");

			courseManager.createIndex(temphold, 23001, 10);
			courseManager.getIndexByIndexNo(23001).addTutTimeSlot(TimeSlot.DAY.FRI, 14, 30, 16, 30, "TR+3");
		}

		/*userManager.addStudent("qingru", "c160144@e.ntu", "U222222B", 92298224, Student.GENDER.FEMALE, "Singaporean", "qingru", "password");
		userManager.addStudent("student", "qhu003@.e.ntu.edu.sg", "Ustudent", 9999, Student.GENDER.FEMALE,"studentNationality,", "student", "student");
		userManager.addStudent("qinghui", "qlai002@e.ntu.edu.sg", "U1111111B", 93874270, Student.GENDER.MALE, "Singaporean", "qinghui", "password");
		userManager.addStudent("ron", "c160144@e.ntu", "U333333B", 93874270, Student.GENDER.MALE, "Singaporean", "c160144", "password");

		*/
		/*
		courseManager.addCourse("CE2003", "Digital System Design", "SCE");
		courseManager.createIndex(courseManager.getCourseByCourseId("CE2003"), 10042, 2);
		courseManager.createIndex(courseManager.getCourseByCourseId("CE2003"), 10043, 2);
		*/
	}

	public String getCourseOfIndex(int index){
		return courseManager.getCourseByIndexNo(index).getCourseId();
	}
}
