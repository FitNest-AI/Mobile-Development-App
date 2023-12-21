package com.example.fitnestapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseWorkout(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class End(

	@field:SerializedName("right_knee")
	val rightKnee: Int? = null,

	@field:SerializedName("right_hip")
	val rightHip: Int? = null,

	@field:SerializedName("left_hip")
	val leftHip: Int? = null,

	@field:SerializedName("right_elbow")
	val rightElbow: Int? = null,

	@field:SerializedName("left_knee")
	val leftKnee: Int? = null,

	@field:SerializedName("left_elbow")
	val leftElbow: Int? = null
)

data class ExerciseId(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("orientation")
	val orientation: String? = null,

	@field:SerializedName("instruction")
	val instruction: String? = null,

	@field:SerializedName("levelId")
	val levelId: LevelId? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("start")
	val start: Start? = null,

	@field:SerializedName("end")
	val end: End? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("targetMuscleId")
	val targetMuscleId: List<TargetMuscleIdItem?>? = null,

	@field:SerializedName("desc")
	val desc: String? = null,

	@field:SerializedName("direction")
	val direction: String? = null
)

data class Data(

	@field:SerializedName("workout")
	val workout: List<WorkoutItem?>? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("current_page")
	val currentPage: Int? = null
)

data class TargetMuscleIdItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("_id")
	val id: String? = null
)

data class MovesetItem(

	@field:SerializedName("set")
	val set: Int? = null,

	@field:SerializedName("exerciseId")
	val exerciseId: ExerciseId? = null,

	@field:SerializedName("rep")
	val rep: Int? = null
)

data class WorkoutItem(

	@field:SerializedName("rest")
	val rest: Int? = null,

	@field:SerializedName("level")
	val level: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("day")
	val day: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("moveset")
	val moveset: List<MovesetItem?>? = null,

	@field:SerializedName("point")
	val point: Point? = null,

	@field:SerializedName("desc")
	val desc: String? = null
)

data class LevelId(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("_id")
	val id: String? = null
)

data class Start(

	@field:SerializedName("right_knee")
	val rightKnee: Int? = null,

	@field:SerializedName("right_hip")
	val rightHip: Int? = null,

	@field:SerializedName("left_hip")
	val leftHip: Int? = null,

	@field:SerializedName("right_elbow")
	val rightElbow: Int? = null,

	@field:SerializedName("left_knee")
	val leftKnee: Int? = null,

	@field:SerializedName("left_elbow")
	val leftElbow: Int? = null
)

data class Point(

	@field:SerializedName("Quadriceps")
	val quadriceps: Int? = null,

	@field:SerializedName("shoulders")
	val shoulders: Int? = null,

	@field:SerializedName("triceps")
	val triceps: Int? = null,

	@field:SerializedName("chest")
	val chest: Int? = null,

	@field:SerializedName("forearms")
	val forearms: Int? = null,

	@field:SerializedName("abs")
	val abs: Int? = null,

	@field:SerializedName("hamstrings")
	val hamstrings: Int? = null,

	@field:SerializedName("hips")
	val hips: Int? = null,

	@field:SerializedName("legs")
	val legs: Int? = null,

	@field:SerializedName("back")
	val back: Int? = null,

	@field:SerializedName("biceps")
	val biceps: Int? = null,

	@field:SerializedName("glutes")
	val glutes: Int? = null
)
