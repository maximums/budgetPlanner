# Budget Planner

Budget Planner is an Android app intended to help users better manage their budget by offering them information about monthly incomes and expenses.

## Installation

Clone this repository and import it into __Android Studio__
`git clone https://github.com/maximums/budgetPlanner.git`

## Android Version Targeting

Budget Planner is built to work with Android API 31(S).__However__ minimum SDK support is 23(Marshmallow).

## Build configuration

This project was build on JDK 1.8

## Build and Run:

From Android studio:
1. ___Run___ menu
2. ___Run 'app'___

## Generating APK

From Android studio:
1. ___Build___ menu
2. ___Build Bundle(s)/APK(s)___
3. Generated APK is located in ___[Project directory]\app\build\outputs\apk\debug___

## Version control workflow

We loosely use the "Git flow" approach: main is the release
branch - it should always be releasable, and only merged into
when we have tested and verified that everything works and is
good to go.

Daily development is done in the development branch. Features,
bugfixes and other tasks are done as branches off of develop,
then merged back into develop directly or via pull requests.

