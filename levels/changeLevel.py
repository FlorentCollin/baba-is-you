def changeLevel(nameLevel):
    with open(nameLevel+".txt", "r") as lvl:
        with open("new_"+nameLevel+".txt", "w") as new_lvl:
            i=0
            for line in lvl:
                if i==0 or i==1:
                    i+=1
                    new_lvl.write(line.strip()+"\n")
                else:
                    line = line.strip().split()
                    if line[0] == "text_wall":
                        line[0] = "TextWall"
                    elif line[0] == "text_rock":
                        line[0] = "TextRock"
                    elif line[0] == "text_baba":
                        line[0] = "TextBaba"
                    elif line[0] == "text_goop":
                        line[0] = "TextGoop"
                    elif line[0] == "text_flag":
                        line[0] = "TextFlag"
                    elif line[0] == "is":
                        line[0] = "TextIs"
                    elif line[0] == "win":
                        line[0] = "TextWin"
                    elif line[0] == "push":
                        line[0] = "TextPush"
                    elif line[0] == "stop":
                        line[0] = "TextStop"
                    elif line[0] == "you":
                        line[0] = "TextYou"
                    elif line[0] == "sink":
                        line[0] = "TextSink"
                    else:
                        line[0] = line[0].capitalize()
                    new_lvl.write(line[0]+" "+line[1]+" "+line[2]+"\n")

changeLevel("lvl1")
changeLevel("lvl2")
changeLevel("lvl3")
changeLevel("lvl4")