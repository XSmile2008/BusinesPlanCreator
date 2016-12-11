# BusinesPlanCreator
<h1>Програмний продукт для забезпечення процедури автоматизованого створення бізнес плану</h1> 
Необхідно створити експертну систему яка дозволяє:<br>
1.	На основі даних, що вводить користувач запропонувати на вибір галузь діяльності для бізнесу користувача. Галузі повинні бути посортовані від найбільш прибуткової до найменш. <br>
2.	Під час вибору користувач повинен отримати розгорнуту інформацію про вид підприємницької діяльності.<br>
3.	Після того як користувач вибере діяльність і введе дані, що необхідні для формування плану програма вибере необхідний шаблон бізнес плану та зробить необхідно розрахунки.<br>
4.	Програма видає на виході файл з готовим бізнес планом. <br>
Документації, книги та шаблони знаходяться у папці на Дропбоксі за посиланням: https://www.dropbox.com/sh/i18jy83fpjuw2t9/AAA6n58HvS7K2PKXjsK-HWkFa?dl=0

<h1>Методологічні пояснення</h1>
https://ukrstat.org/uk/operativ/operativ2012/sze/sze_met.html

<h1>Архітектура програмного продукту, хто за неї відповідає та особливості частин</h1>

<h2>Парсер сайту /ukrstat.org</h2>
Автори Інна Бакум та Ірина Маленко

Окремий програмний продукт, який формує json з статистичними даними, доступними за наступними посиланнями:
<h3>Оброблені</h3>
Кількість підприємств
http://ukrstat.org/uk/operativ/operativ2013/fin/kp_ed/kp_ed_u/kp_ed_u_2015.htm  
Кількість суб’єктів господарювання
http://ukrstat.org/uk/operativ/operativ2014/fin/osp/ksg/ksg_u/ksg_u_14.htm  
Кількість зайнятих працівників
http://ukrstat.org/uk/operativ/operativ2014/fin/osp/kzp/kzp_u/kzp_u_14.htm  

<h3>Необхідно обробити</h3>

Рентабельність
https://ukrstat.org/uk/operativ/operativ2016/fin/rodp/rodp_ed/rodp_ed_u/rodp_ed_0116_u.htm  
Обсяг реалізованої продукції
https://ukrstat.org/uk/operativ/operativ2013/fin/kp_ed/kp_ed_u/orp_ed_u_2015.htm  
Обсяг виробленої продукції
https://ukrstat.org/uk/operativ/operativ2015/fin/ovpp/ovpp_2014_u.htm  
Чистий прибуток (збиток)
https://ukrstat.org/uk/operativ/operativ2016/fin/chpr/chpr_ed/chpr_ed_u/chpr_ed_0116_u.htm  
Витрати на персонал
https://ukrstat.org/uk/operativ/operativ2013/fin/kp_ed/kp_ed_u/vpp_ed_u_2015.htm  
