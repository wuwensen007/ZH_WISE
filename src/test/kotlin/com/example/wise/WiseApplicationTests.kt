package com.example.wise


class WiseApplicationTests {


//	@Autowired
//	var factoryRepository: FactoryRepository? = null
//
//	@Autowired
//	var noteRepository: NoteRepository? = null
//
//	@BeforeTestMethod
//	fun init(){
//	}
//
//	@Test
//	fun contextLoads() {
//
//		//val res = FtpUtil.checkFileInFtp("", "MESProductList_YX8.csv")
////		println(FtpUtil.TEMP_WISE_FILE_DIR)
////		println(File.separator)
////		FtpUtil.login()
////		FtpUtil.downloadCSVFiles(csvFileName = CustomConfig.yx8csvname)
////		println(FtpUtil.downloadSubSystemFiles("""${CustomConfig.yx8path}${File.separator}${CustomConfig.sm}"""))
//	}
//
//
//	@Test
//	fun test1(){
//
//		val factory = YX8SubSystemFactory()
////		factory.getSubSystems().forEach {
////			val system = it
////			if (system is DesktopSubSystem && system is OnCall){
////				println(it.getRunCommand())
////				it.runSystem()
////			}
////		}
//	}
//
//	@Test
//	fun test2(){
////		val mesProductList: List<MESProduct> = CsvToBeanBuilder<MESProduct>(FileReader("""${Constant.WISE_LOCAL_CSV_TEMP_DIR}${File.separator}${CustomConfig.yx8csvname}""")).withType(MESProduct::class.java).build().parse()
////		val subSystem = mesProductList.filter { it.systemName == "OPI" }.map {
////			OPI(it.systemName, it.fileName, it.version, it.updateTime, FAB.YX8, YX8SubSystemVer())
////		}.first()
////		println(subSystem)
////		subSystem.runSystem()
//	}
//
//
//	@Test
//	fun test3(){
//
//		val factory = YX8SubSystemFactory()
//		val subSystems = factory.getSubSystems()
//		val oncall = subSystems["ONCALL"]
////		println("原系统版本信息:${oncall?.systemVersion}")
////		if (oncall is DesktopSubSystem){
////			oncall.updateSystem()
////		}
////		println("新系统版本信息:${oncall?.systemVersion}")
//	}
//
//
//	@Test
//	fun test4(){
//
////		val ftpClient1 = FTPClient()
////		var ftpClient2 = FTPClient()
////		FtpUtil.login(ftpClient1)
////		FtpUtil.login(ftpClient2)
////		val path = """${CustomConfig.basepath}${File.separator}${CustomConfig.yx8path}${File.separator}${CustomConfig.oncall}"""
////		ftpClient1.changeWorkingDirectory("""WISE\YX8SetUp\SPC\app.publish\""")
////		ftpClient1.listFiles().forEach {
////			println(it.name)
////		}
////		ftpClient2.changeWorkingDirectory(path)
//
//	}
//
//
//	@Test
//	fun test6(){
//
//
//		backUpLocalFiles("D:\\WISE_ROOT\\WISE\\YX8SetUp\\SPC", "D:\\WISE_ROOT\\WISE\\YX8BackUp\\SPC")
//
//	}
//
//	private fun backUpLocalFiles(from: String, toDir: String){
//
//		val file = File(from)
//		if (!file.exists()){
//			return
//		}
//		val to = File(toDir)
//		if (!to.exists()){
//			to.mkdirs()
//		}
//		file.listFiles().forEach {
//			if (it.isFile){
//				// 创建文件
//				val newFile = File("$toDir${File.separator}${it.name}")
//				Files.copy(it.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
//			}else if(it.isDirectory){
//				val newDir = File("$toDir${File.separator}${it.name}")
//				newDir.mkdirs()
//				backUpLocalFiles(it.canonicalPath, newDir.canonicalPath)
//			}
//		}
//
//
//	}
//
//
//	@Test
//	fun test07(){
//
//		factoryRepository?.save(Factory(fab_name = FAB.TJ12.name, desc = FAB.TJ12.toString()))
//	}
//
//	@Test
//	fun test08(){
//
//		factoryRepository?.findAll()?.forEach {
//			println(it)
//		}
//	}
//
//	@Test
//	fun test09(){
//		with(factoryRepository?.findAll()){
//			println(this)
//		}
//	}
//
//	@Test
//	fun test10(){
//		val fab = factoryRepository?.findByFabName(FAB.TJ12.name)
//		noteRepository?.save(Note(noteinfo = "公告标题5#公告内容-5---------", factory = fab!!, type = "一般公告"))
//	}
//
//	@Test
//	fun test11(){
//		noteRepository?.findAll()?.forEach {
//			println(it.factory)
//		}
//	}
//
//	@Test
//	fun test12(){
//		factoryRepository?.findByFabName(FAB.YX12.name)?.notes?.forEach {
//			println(it)
//		}
//	}
//
//
//	@Test
//	fun test13(){
//		val factory = factoryRepository?.findByFabName(FAB.YX8.name)
//		val note = Note(noteinfo = "Hello4#World4------", type = "一般公告", factory = factory!!)
//			noteRepository?.save(note)
//	}
//
//
////	@Test
////	fun test14(){
////		val notes = noteRepository?.findByFab(1)?.content
////		notes?.forEach {
////			println(it)
////		}
////
////	}
//
//	@Test
//	fun test15(){
//
//	}


}
