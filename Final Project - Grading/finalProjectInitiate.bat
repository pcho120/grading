@echo off
cd C:\
if not exist "C:\CSET4250_FinalProject_HyunkunCho\" mkdir C:\CSET4250_FinalProject_HyunkunCho
cd %USERPROFILE%\Downloads
move final_project C:\CSET4250_FinalProject_HyunkunCho
cd C:\CSET4250_FinalProject_HyunkunCho\final_project
javac grading_main.java
java grading_main
pause 