# hydrology_common_java
该库适用于西安理工大学水利水电学院水利信息化团队研一培训内容
各文件夹介绍
- datasets：相关计算所需数据
- results：相关计算输出结果
- xmlFiles：相关计算所需参数及用于关联datasets中xls表供更改
- utils：相关计算所创建的工具类，包括list数组操作工具类、Xml文件读取类、Excel文件读取类
- src/domain：相关计算所创建的对象
- src/exercise：相关计算的实现类
实现类介绍：
- CopForNRegulation：规定出力的水能计算
- Evapor3RCount：三层蒸发的产流计算
- FloodRegulation：洪水调节计算
- hydroFreq：水文频率计算，修改中
- IsoForNRegulation：等流量的水能调节计算
- IsoRegulation：等流量调节计算
- MaskingenCalculus：马斯京根法(朴素版~.~)
- PeriodUnitLine：时段单位线
- RegulationStorage：列表法求年调节水库的调节库容
- XajModeMain：新安江水文模型，还在调试中不知道哪有问题=.=
