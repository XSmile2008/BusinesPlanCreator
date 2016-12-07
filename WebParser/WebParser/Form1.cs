using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;
using System.Windows.Forms;
using Newtonsoft.Json;
using xNet;
namespace WebParser
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        string ConvertSource(String sourceWebPage)
        {
            File.WriteAllText(AppDomain.CurrentDomain.BaseDirectory + "Html.txt", sourceWebPage, Encoding.GetEncoding(1251));
            String text = "";
            using (StreamReader sr = File.OpenText(AppDomain.CurrentDomain.BaseDirectory + "Html.txt"))
            {
                var temp = "";
                while ((temp = sr.ReadLine()) != null)
                {
                    text += temp;
                }
            }
            return text;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            HtmlToJson json3 = new HtmlToJson();
            json3.Link = textBox1.Text;
            using (var request = new HttpRequest())
            {
                String sourceWebPage = request.Get(textBox1.Text).ToString();
                sourceWebPage = ConvertSource(sourceWebPage);
                String pattern1 = @"<font size=" + "\"" + "2" + "\">" + "(.*?)<" + "\\/font>";
                RegexOptions options = RegexOptions.Singleline | RegexOptions.Multiline;
                Match m = Regex.Match(sourceWebPage, pattern1, options);
   
                if (textBox1.Text.Contains("kzp_u_14.htm"))
                {
                    json3 = GetJsonHtml3(m);
                    json3?.Datas.Remove(json3.Datas[json3.Datas.Count - 1]);
                    OutputJSON(json3);
                }
                else if (textBox1.Text.Contains("ksg_u_14.htm"))
                {
                    json3 = GetJsonHtml2(m);
                    OutputJSON(json3);
                }
                else if (textBox1.Text.Contains("kp_ed_u_2015.htm"))
                {
                    json3 = GetJsonHtml1(m);
                    json3?.Datas.Remove(json3.Datas[json3.Datas.Count - 1]);
                    OutputJSON(json3);
                }
            }



        }

        public void OutputJSON(HtmlToJson json3)
        {
           
            string output = JsonConvert.SerializeObject(json3);
            File.WriteAllText(AppDomain.CurrentDomain.BaseDirectory + "JsonData.json", output, Encoding.GetEncoding(1251));
            textBox2.Clear();
            textBox2.Text = output;
        }

        public int GetCountMatch(Match m)
        {
            int count = 0;
            while (m.Success)
            {
                count++;

                m = m.NextMatch();
            }
            return count;
        }

        public HtmlToJson GetJsonHtml1(Match m)
        {

            HtmlToJson json3 = new HtmlToJson();
            int lengthMatch = GetCountMatch(m);
            for (int i = 0; i < lengthMatch; i++)
            {

                if (i >= 17 )
                {
                    Data tmpData = new Data();
                    tmpData.NameRow = m.Groups[1].Value.Replace("\t", String.Empty);
                    m = (i == 17) ? m.NextMatch() : m;
                    for (int j = 0; j < 9; j++)
                    {
                        i++;
                        m = m.NextMatch();
                        Column tmpColumn = new Column();

                        switch (j)
                        {
                            case 0:
                                tmpColumn.NameColumn = "Усього одиниць";
                                break;
                            case 1:
                                tmpColumn.NameColumn = "Великі підприємства, одиниць";
                                break;
                            case 2:
                                tmpColumn.NameColumn = "Великі підприємства, у відсотках до загальної кількості";
                                break;
                            case 3:
                                tmpColumn.NameColumn = "Середні підприємства, одиниць";
                                break;
                            case 4:
                                tmpColumn.NameColumn = "Середні підприємства, у відсотках до загальної кількості";
                                break;
                            case 5:
                                tmpColumn.NameColumn = "Малі підприємства, одиниць";
                                break;
                            case 6:
                                tmpColumn.NameColumn = "Малі підприємства, у відсотках до загальної кількості";
                                break;
                            case 7:
                                tmpColumn.NameColumn = "Мікропідприємства, одиниць";
                                break;
                            case 8:
                                tmpColumn.NameColumn = "Мікропідприємства, у відсотках до загальної кількості";
                                break;
                           
                        }
                        try
                        {
                            tmpColumn.Value = Convert.ToSingle(m.Groups[1].Value);
                        }
                        catch
                        {
                            tmpColumn.Value = 0;
                        }

                        tmpData.Columns.Add(tmpColumn);
                    }
                    json3.Datas.Add(tmpData);
                }
                m = m.NextMatch();
            }
            json3.Link = textBox1.Text;
            return json3;
        }
        public HtmlToJson GetJsonHtml2(Match m)
        {
            HtmlToJson json3 = new HtmlToJson();
            int lengthMatch = GetCountMatch(m);
            for (int i = 0; i < lengthMatch; i++)
            {

                if (i == 10||i>17)
                {
                    Data tmpData = new Data();
                    tmpData.NameRow = m.Groups[1].Value.Replace("\t", String.Empty);
                    m =(i == 10)?m.NextMatch():m;
                    for (int j = 0; j < 5; j++)
                    {
                        i++;
                        m = m.NextMatch();
                        Column tmpColumn = new Column();

                        switch (j)
                        {
                            case 0:
                                tmpColumn.NameColumn = "Усього, тис. осіб";
                                break;
                            case 1:
                                tmpColumn.NameColumn = "Підприємства, тис. осіб";
                                break;
                            case 2:
                                tmpColumn.NameColumn = "Підприємства, у відсотках до загальної кількості";
                                break;
                            case 3:
                                tmpColumn.NameColumn = "Фізичні особи-підприєпмці, тис. осіб";
                                break;
                            case 4:
                                tmpColumn.NameColumn = "Фізичні особи-підприєпмці, у відсотках до загальної кількості";
                                break;
                        }
                        try
                        {
                            tmpColumn.Value = Convert.ToSingle(m.Groups[1].Value);
                        }
                        catch
                        {
                            tmpColumn.Value = 0;
                        }

                        tmpData.Columns.Add(tmpColumn);
                    }
                    json3.Datas.Add(tmpData);
                }else if (i == 17)
                {
                    Data tmpData = new Data();
                    tmpData.NameRow = m.Groups[1].Value.Replace("\t", String.Empty);
                    for (int j = 0; j < 5; j++)
                    {
                        i++;
                       
                        Column tmpColumn = new Column();

                        switch (j)
                        {
                            case 0:
                                tmpColumn.NameColumn = "Усього, тис. осіб";
                                break;
                            case 1:
                                tmpColumn.NameColumn = "Підприємства, тис. осіб";
                                break;
                            case 2:
                                tmpColumn.NameColumn = "Підприємства, у відсотках до загальної кількості";
                                break;
                            case 3:
                                tmpColumn.NameColumn = "Фізичні особи-підприєпмці, тис. осіб";
                                break;
                            case 4:
                                tmpColumn.NameColumn = "Фізичні особи-підприєпмці, у відсотках до загальної кількості";
                                break;
                        }
                        try
                        {
                            tmpColumn.Value = Convert.ToSingle(m.Groups[1].Value);
                        }
                        catch
                        {
                            tmpColumn.Value = 0;
                        }

                        tmpData.Columns.Add(tmpColumn);
                    }
                    json3.Datas.Add(tmpData);
                }
                m = m.NextMatch();
            }
            json3.Link = textBox1.Text;
            return json3;
        }

        public HtmlToJson GetJsonHtml3(Match m)
        {
            HtmlToJson json3 = new HtmlToJson();
            int lengthMatch = GetCountMatch(m);
            for (int i = 0; i < lengthMatch; i++)
            {

                if (i > 10)
                {
                    Data tmpData = new Data();
                    tmpData.NameRow = m.Groups[1].Value.Replace("\t", String.Empty);
                    for (int j = 0; j < 5; j++)
                    {
                        lengthMatch--;
                        m = m.NextMatch();
                        Column tmpColumn = new Column();

                        switch (j)
                        {
                            case 0:
                                tmpColumn.NameColumn = "Усього, тис. осіб";
                                break;
                            case 1:
                                tmpColumn.NameColumn = "Підприємства, тис. осіб";
                                break;
                            case 2:
                                tmpColumn.NameColumn = "Підприємства, у відсотках до загальної кількості";
                                break;
                            case 3:
                                tmpColumn.NameColumn = "Фізичні особи-підприєпмці, тис. осіб";
                                break;
                            case 4:
                                tmpColumn.NameColumn = "Фізичні особи-підприєпмці, у відсотках до загальної кількості";
                                break;
                        }
                        try
                        {
                            tmpColumn.Value = Convert.ToSingle(m.Groups[1].Value);
                        }
                        catch
                        {
                            tmpColumn.Value = 0;
                        }

                        tmpData.Columns.Add(tmpColumn);
                    }
                    json3.Datas.Add(tmpData);
                }
                m = m.NextMatch();
            }
            json3.Link = textBox1.Text;
            return json3;
        }

        public class Column
        {
            public String NameColumn;
            public float Value;
        }

        public class Data
        {
            public String NameRow;
            public List<Column> Columns = new List<Column>();
        }

        public class HtmlToJson
        {
            public String Link;
            public List<Data> Datas = new List<Data>();
        }
    }
}
