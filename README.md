# Automated-TestingTool-2020S2-AdelaideUni

Developed by Wenping (Deb) Du

I developed this tool as a project in University of Adelaide, COMP SCI 7092 - Mobile and Wireless Systems. I have proposed an inspiration idea in this project, which is a universal automated-testing tool for Android applications.

the original link is:
https://github.cs.adelaide.edu.au/2020-Mobile-and-Wireless-Systems/HealthApps

The automated testing tool is developed based on the accessibility module of Android SDK.
The whole files are put into the package which is "com.adelaide.autotest".
You need the following steps to reuse it:
1. Create an Android application firstly, name the package as "com.adelaide.autotest";
2. Put the whole files of the automated testing tool into the directory of this application;
3. create a service in AndroidManifest.xml for the class AutoPayService;
4. create a receiver in AndroidManifest.xml for the class BootReceiver;
5. In class AutoPayService, add the tested APK's package name in variable "info.packageNames";
6. Start with the function "onAccessibilityEvent" of class AutoPayService, then modify the code to meet your request.

Please do not use debug mode during your development, because this model will kill your service.
When you complete the development of your tool and install it on the mobile phone, please manually open the system setting to help your tool get accessibility permission!
Then, as your APK that needs to be tested is launched(you can write the code to launch from your automated testing tool or Frida,...), the automated testing tool can detect the component node, then perform click or other actions.
