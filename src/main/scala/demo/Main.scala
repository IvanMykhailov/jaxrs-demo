package demo;


object Main extends App {
  private val app = new DemoApp()
  println(s"Jersey app started with WADL available at ${app.baseUrl}application.wadl Hit enter to stop it...")
  readLine()
  app.stop()
}

