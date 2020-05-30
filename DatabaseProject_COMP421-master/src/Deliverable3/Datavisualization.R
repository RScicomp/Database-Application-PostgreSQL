#install.packages("RPostgreSQL")
require("RPostgreSQL")
require("shiny")
require("ggplot2")
require("zoo")
require("dplyr")
require(shinyWidgets)



con<-dbConnect(drv = PostgreSQL(), user = "cs421g21", dbname = "cs421", host = "comp421.cs.mcgill.ca",  password = "",port = "5432/cs421")
withdrawals <- dbGetQuery(con, "SELECT tid,dateof,timeof,amt,aidc,statusof FROM t_transactions INNER JOIN t_withdrawals ON tidw = tid;")
withdrawals["Type"] = "withdrawal"
transform(withdrawals, dateof= as.Date(dateof, "%Y%M%D"))


deposits <- dbGetQuery(con, "SELECT tid,dateof,timeof,amt,aid,statusof FROM t_transactions INNER JOIN t_deposits ON tidd = tid;")
deposits["Type"] = "deposit"
deposits["dateof"] <-lapply(withdrawals["dateof"], function(x) as.Date(x, "%Y%M%D"))

colnames(withdrawals) <- c("tid", "dateof","timeof","amt","aidc","statusof","type")
colnames(deposits) <- c("tid", "dateof","timeof","amt","aidc","statusof","type")
customers <- unique(sort(dbGetQuery(con, "SELECT aidc FROM t_transactions INNER JOIN t_withdrawals ON tidw = tid;")$aidc))

data <- rbind(withdrawals, deposits)
maxw<- max(withdrawals$dateof)
maxd<- max(deposits$dateof)
maxdate <- as.Date(max(maxw,maxd))
mindate <- as.Date(min(min(withdrawals$dateof), min(deposits$dateof)))

ui <- fluidPage(
  
  
  plotOutput("plot1", click = "plot_click"),
  verbatimTextOutput("info"),
  fluidRow(
    column(3, sliderInput("time", "Time:",  min = as.Date(mindate,"%Y-%m-%d"), max = as.Date(maxdate,"%Y-%m-%d"), value = c(as.Date(mindate,"%Y-%m-%d"),as.Date(maxdate,"%Y-%m-%d")), timeFormat="%Y-%m-%d")),
    column(3,selectInput("quarterly", "Quarter",
                         c("Yearly" = "Yearly","Quarter" = "Quarter", "Non-Quarterly" = "Non-Quarterly", "Monthly" ="Monthly"))),
    column(3,dropdownButton("customers", label = "Customers", circle = FALSE,
                            checkboxGroupInput('customers', 'customer selection', as.list(customers), selected = as.list(customers)))),                                          # unique(as.character(as.list(customers))),selected = unique(as.character(as.list(customers))))))
    column(3,selectInput("all", "Select All", c("All" = "All","Deselect" = "None")))
  )
)

server <- function(input, output,session) {
  observe({
    x <- input$all
    
    # Can use character(0) to remove all choices
    if (x == "All"){

      updateCheckboxGroupInput(session, "customers",
                               label = paste("customers", length(customers)),
                               choices = customers,
                               selected = customers)
    }else{

      updateCheckboxGroupInput(session, "customers",
                               label = paste("customers", length(customers)),
                               choices = customers,
                               selected = NULL)
    }
    
    # Can also set the label and select items
    
    
  })
  
  
  output$plot1 <- renderPlot({
    theme_bw(base_size = 8, base_family = "")
    # dataw<-subset(withdrawals, dateof> input$time[1] & dateof< input$time[2])
    # datad<-subset(deposits, dateof> input$time[1] & dateof< input$time[2])
    # if (input$quarterly == "Quarter"){
    #   dataw$dateof=as.yearqtr(as.Date(dataw$dateof, "%Y-%M-%D"))
    #   datad$dateof=as.yearqtr(as.Date(datad$dateof, "%Y-%M-%D"))
    # }
    # 
    # dataw$dateof <- as.Date(dataw$dateof)
    # datad$dateof <- as.Date(datad$dateof)
    # #browser()
    datas<-filter(data,data$aidc %in% input$customers)
    if (length(input$customers) == 1){
      width = 20
    }else{
      width = 10
    }
    datas<-subset(datas, dateof> input$time[1] & dateof< input$time[2])
    
    # p <- ggplot() + geom_line(data = dataw, aes(x = dateof, y = amt), color = "red") +
    #   geom_line(data = datad, aes(x = dateof, y = amt), color = "blue") +
    #   xlab('Dates') +
    #   ylab('Total Amounts')+
    #   scale_x_date(date_labels="%Y", date_breaks  ="1 year")+
    #   stat_summary(fun.y=sum, geom="line") +
    #   stat_summary(fun.y=sum, geom="point")
    
    p <- ggplot(datas, aes(dateof, amt,fill=as.factor(type))) + geom_bar(stat= 'identity', width = width)
     # scale_x_date(breaks = as.Date(c("1970-01-01", "2000-01-01")))
    #p <- ggplot() + geom_bar(data = withdrawals, aes(x = dateof, y = amt), color = "blue") +geom_bar(data = deposits, aes(x = dateof, y = amt), color = "red") + xlab('Dates') + ylab('Amounts')
    

    # ggplot(withdrawals, aes(dateof, amt)) + geom_line()+geom_line(data = prescription2, aes(x = dates, y = Difference), color = "red") +
    #   xlab('Dates') +
    #   ylab('percent.change')
    #plot(withdrawals$amt, withdrawals$dateof)
    p
    
    
  })
  
  
}

shinyApp(ui, server)
dbDisconnect(con)
